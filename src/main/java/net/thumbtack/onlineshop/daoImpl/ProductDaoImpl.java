package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductDaoImpl extends DaoImplBase implements ProductDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

    public void addProduct(String cookieValue, Product product, List<Integer> categoriesId) throws OnlineShopException {
        LOGGER.debug("DAO add Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (!user.getUserType().equals(UserType.ADMIN.name())) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                        null,
                        OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
            }
            try {
                getProductMapper(sqlSession).addProduct(product);
                if (!categoriesId.isEmpty()) {
                    getProductMapper(sqlSession).addProductCategories(categoriesId, product);
                    List<Category> categories = getCategoryMapper(sqlSession).getCategories(categoriesId);
                    product.setCategories(categories);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }


    public void editProduct(String cookieValue, Product product, List<Integer> categoriesId) throws OnlineShopException {
        LOGGER.debug("DAO edit Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (!user.getUserType().equals(UserType.ADMIN.name())) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                        null,
                        OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
            }
            try {
                getProductMapper(sqlSession).editProduct(product);
                if (categoriesId != null) {
                    getProductMapper(sqlSession).deleteAllProductCategories(product);
                    if (!categoriesId.isEmpty()) {
                        getProductMapper(sqlSession).addProductCategories(categoriesId, product);
                    }
                }
                List<Category> categories = getProductMapper(sqlSession).getProductCategories(product.getId());
                product.setCategories(categories);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't edit Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }


    public void deleteProduct(String cookieValue, Integer id) throws OnlineShopException {
        LOGGER.debug("DAO delete Product with id {}", id);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (!user.getUserType().equals(UserType.ADMIN.name())) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                        null,
                        OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
            }
            try {
                getProductMapper(sqlSession).deleteProduct(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }


    public Product getProduct(String cookieValue, Integer id) throws OnlineShopException {
        LOGGER.debug("DAO get Product with id {}", id);
        Product product;
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (!user.getUserType().equals(UserType.ADMIN.name())
                    || !user.getUserType().equals(UserType.CLIENT.name())) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_ACCESS_PERMISSION,
                        null,
                        OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText() + "or" + OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());
            }
            try {
                product = getProductMapper(sqlSession).getProduct(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return product;
    }

    @Override
    public List<Product> getProductsByCategory(String cookieValue, List<Integer> categoriesId, String order) throws OnlineShopException {
        LOGGER.debug("DAO get Products");
        List<Product> products = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (user.getUserType().equals(UserType.ADMIN.name())
                    || user.getUserType().equals(UserType.CLIENT.name())) {
                try {
                    if (categoriesId == null) {
                        if (order.equals("product")) {
                            return getProductMapper(sqlSession).getAllProductsByProductOrder();
                        }
                        if (order.equals("category")) {
                            List<Category> allCategories = getCategoryMapper(sqlSession).getAllCategories();
                            List<Category> allCategoriesSortedByName = new ArrayList<>();
                            allCategories.stream().sorted(Comparator.comparing(Category::getName)).forEach(allCategoriesSortedByName::add);
                            products.addAll(getProductMapper(sqlSession).getProductsWithoutCategories());
                            for (Category category : allCategoriesSortedByName) {
                                List<Product> categoryProducts = getProductMapper(sqlSession).getProductsByCategory(category.getId());
                                for (Product product : categoryProducts) {
                                    product.setCategories(new ArrayList<>());
                                    product.getCategories().add(category);
                                }
                                products.addAll(categoryProducts);
                            }
                            return products;
                        }
                    } else {
                        if (categoriesId.size() == 0) {
                            return getProductMapper(sqlSession).getProductsWithoutCategories();
                        } else {
                            if (order.equals("product")) {
                                return getProductMapper(sqlSession).getProductsByCategoriesByProductOrder(categoriesId);
                            }
                            if (order.equals("category")) {
                                List<Category> categories = getCategoryMapper(sqlSession).getCategories(categoriesId);
                                List<Category> categoriesSortedByName = new ArrayList<>();
                                categories.stream().sorted(Comparator.comparing(Category::getName)).forEach(categoriesSortedByName::add);
                                for (Category category : categoriesSortedByName) {
                                    List<Product> categoryProducts = getProductMapper(sqlSession).getProductsByCategory(category.getId());
                                    for (Product product : categoryProducts) {
                                        product.setCategories(new ArrayList<>());
                                        product.getCategories().add(category);
                                    }
                                    products.addAll(categoryProducts);
                                }
                                return products;
                            }
                        }
                    }
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't get Products. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
            } else {
                throw new OnlineShopException(OnlineShopErrorCode.USER_ACCESS_PERMISSION,
                        null,
                        OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText() + "or" + OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());

            }
        }
        return products;
    }

    @Override
    public List<Product> addProductInBasket(String cookieValue, Product basketProduct) throws OnlineShopException {
        LOGGER.debug("DAO add Product {} in basket", basketProduct);
        List<Product> basketProducts;
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (!user.getUserType().equals(UserType.CLIENT.name())) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_CLIENT,
                        null,
                        OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());
            }
            try {
                Product product = getProductMapper(sqlSession).getProduct(basketProduct.getId());
                if (product == null) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                            "id",
                            OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());
                }
                if (!product.getName().equals(basketProduct.getName())) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                            "name",
                            OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + product.getName());
                }
                if (product.getPrice() != (basketProduct.getPrice())) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                            "price",
                            OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + product.getPrice());
                }
                Product productFromBasket = getBasketMapper(sqlSession).getClientProduct(user, basketProduct);
                if (productFromBasket != null) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_DUPLICATE,
                            "id",
                            OnlineShopErrorCode.PRODUCT_DUPLICATE.getErrorText());
                }
                getBasketMapper(sqlSession).addProductInBasket(basketProduct, user);
                basketProducts = getBasketMapper(sqlSession).getClientBasket(user);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return basketProducts;
    }


    public void deleteProductFromBasket(String cookieValue, Integer productId) throws OnlineShopException {
        LOGGER.debug("DAO delete Product from Basket with productId {}", productId);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (!user.getUserType().equals(UserType.CLIENT.name())) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_CLIENT,
                        null,
                        OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());
            }
            try {
                getBasketMapper(sqlSession).deleteProductFromBasket(user.getId(), productId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }


}
