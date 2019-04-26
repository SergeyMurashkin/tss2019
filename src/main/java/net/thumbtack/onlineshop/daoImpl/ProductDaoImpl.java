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

    public void addProduct(Product product, List<Integer> categoriesId) {
        LOGGER.debug("DAO add Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).addProduct(product);
                if(!categoriesId.isEmpty()) {
                    getProductMapper(sqlSession).addProductCategories(categoriesId, product);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public Product getProduct(Integer id){
        LOGGER.debug("DAO get Product with id {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProduct(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    public void editProduct(Product product, List<Integer> categoriesId) throws OnlineShopException {
        LOGGER.debug("DAO edit Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            try {
                if(getProductMapper(sqlSession).editProduct(product)==1) {
                    if (categoriesId != null) {
                        getProductMapper(sqlSession).deleteAllProductCategories(product);
                        if (!categoriesId.isEmpty()) {
                            getProductMapper(sqlSession).addProductCategories(categoriesId, product);
                        }
                    }
                } else {
                    throw new OnlineShopException(OnlineShopErrorCode.TRANSACTION_CONFLICT,
                            null,
                            OnlineShopErrorCode.TRANSACTION_CONFLICT.getErrorText());
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't edit Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public void deleteProduct(Integer id) {
        LOGGER.debug("DAO delete Product with id {}", id);
        try (SqlSession sqlSession = getSession()) {
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

    @Override
    public List<Product> getProductsByCategory(List<Integer> categoriesId, String order) {
        LOGGER.debug("DAO get Products");
        List<Product> products = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
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


        }
        return products;
    }

    @Override
    public boolean checkIsProductInClientBasket(Client client, Product productToBasket) {
        LOGGER.debug("DAO add Product {} in basket", productToBasket);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getBasketMapper(sqlSession).checkIsProductInClientBasket(client, productToBasket);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void addProductInBasket(Client client, Product productToBasket) {
        LOGGER.debug("DAO add Product {} in basket", productToBasket);
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).addProductInBasket(client, productToBasket);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public List<Product> getClientBasket(Client client) {
        LOGGER.debug("DAO get client basket.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getBasketMapper(sqlSession).getClientBasket(client);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get client basket. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Product getBasketProduct(Client client, Product product) {
        LOGGER.debug("DAO get client basket product.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getBasketMapper(sqlSession).getBasketProduct(client, product);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get client basket product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    public void deleteProductFromBasket(Client client, Integer productId){
        LOGGER.debug("DAO delete Product from Basket with productId {}", productId);
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).deleteProductFromBasket(client.getId(), productId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public List<Product> changeBasketProductQuantity(Client client, Product newBasketProduct) {
        LOGGER.debug("DAO change product quantity {}", newBasketProduct);
        List<Product> basketProducts;
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).changeProductQuantity(client, newBasketProduct);
                basketProducts = getBasketMapper(sqlSession).getClientBasket(client);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't change product quantity. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return basketProducts;
    }



}
