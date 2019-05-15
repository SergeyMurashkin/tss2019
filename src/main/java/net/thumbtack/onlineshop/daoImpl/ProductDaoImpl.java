package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProductDaoImpl extends DaoImplBase implements ProductDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

    public void addProduct(Product product, List<Integer> categoriesId) throws OnlineShopException {
        LOGGER.debug("DAO add Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).addProduct(product);
                if (categoriesId != null && !categoriesId.isEmpty()) {
                    try {
                        getProductMapper(sqlSession).addProductCategories(product, categoriesId);
                    } catch (PersistenceException ex) {
                        LOGGER.error("Can't add product categories. {}", ex);
                        sqlSession.rollback();
                        List<Integer> realCategoriesId = getCategoryMapper(sqlSession).getRealCategoriesId(categoriesId);
                        categoriesId.removeAll(realCategoriesId);
                        throw new OnlineShopException(
                                OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                                "categoriesId",
                                OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText() + " Nonexistent categories: " + categoriesId);
                    }
                }
            } catch (RuntimeException ex) {
                LOGGER.error("Can't add Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public Product getProduct(Integer id) throws OnlineShopException {
        LOGGER.debug("DAO get Product with id {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Product product = getProductMapper(sqlSession).getProduct(id);
                if (product == null) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                            "number of product in address line",
                            OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());
                }
                return product;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    public void editProduct(Product product, List<Integer> categoriesId) throws OnlineShopException {
        LOGGER.debug("DAO edit Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            try {
                if (getProductMapper(sqlSession).editProduct(product) == 1) {
                    if (categoriesId != null) {
                        getProductMapper(sqlSession).deleteAllProductCategories(product);
                        if (!categoriesId.isEmpty()) {
                            try {
                                getProductMapper(sqlSession).addProductCategories(product, categoriesId);
                            } catch (PersistenceException ex) {
                                LOGGER.error("Can't add product categories. {}", ex);
                                sqlSession.rollback();
                                List<Integer> realCategoriesId = getCategoryMapper(sqlSession).getRealCategoriesId(categoriesId);
                                categoriesId.removeAll(realCategoriesId);
                                throw new OnlineShopException(
                                        OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                                        "categoriesId",
                                        OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText() + " Nonexistent categories: " + categoriesId);
                            }
                        }
                    }
                } else {
                    LOGGER.error("Can't edit Product.");
                    sqlSession.rollback();
                    throw new OnlineShopException(
                            OnlineShopErrorCode.PRODUCT_STATE_CHANGING,
                            null,
                            OnlineShopErrorCode.PRODUCT_STATE_CHANGING.getErrorText());
                }
            } catch (RuntimeException ex) {
                LOGGER.error("Can't edit Product. {}", ex);
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
                LOGGER.error("Can't delete Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Product> getAllProductsByProductOrder() {
        LOGGER.debug("DAO get Products");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getAllProductsByProductOrder();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get Products. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Product> getAllProductsByCategoryOrder() {
        LOGGER.debug("DAO get Products");
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Product> products = new ArrayList<>(getProductMapper(sqlSession).getProductsWithoutCategories());
                List<Category> allProductCategoriesSortedByName =
                        getCategoryMapper(sqlSession).getAllCategoriesAndSubCategoriesSortedByName();
                for (Category category : allProductCategoriesSortedByName) {
                    List<Product> categoryProducts = getProductMapper(sqlSession).getProductsByCategory(category.getId());
                    for(Product product : categoryProducts){
                        product.setCategories(Collections.singletonList(category));
                    }
                    products.addAll(categoryProducts);
                }
                return products;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get Products. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Product> getProductsWithoutCategories() {
        LOGGER.debug("DAO get Products");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProductsWithoutCategories();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get Products. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Product> getProductsByCategoryOrder(List<Integer> categoriesId) {
        LOGGER.debug("DAO get Products");
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Product> products = new ArrayList<>();
                List<Category> categoriesSortedByName = getCategoryMapper(sqlSession).getCategories(categoriesId);
                for (Category category : categoriesSortedByName) {
                    List<Product> categoryProducts = getProductMapper(sqlSession).getProductsByCategory(category.getId());
                    for(Product product : categoryProducts){
                        product.setCategories(Collections.singletonList(category));
                    }
                    products.addAll(categoryProducts);
                }
                return products;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get Products. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }

    }

    @Override
    public List<Product> getProductsByProductOrder(List<Integer> categoriesId) {
        LOGGER.debug("DAO get Products");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProductsByCategoriesByProductOrder(categoriesId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get Products. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Integer> getAllProductsId() {
        LOGGER.debug("DAO get all products Id.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getAllProductsId();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get all products Id. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Integer> getProductsIdByCategories(List<Integer> categoriesId) {
        LOGGER.debug("DAO get all products Id.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProductsIdByCategories(categoriesId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get all products Id. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Integer> getProductsIdByCategoriesAndWithout(List<Integer> categoriesId) {
        LOGGER.debug("DAO get all products Id.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProductsIdByCategoriesAndWithout(categoriesId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get all products Id. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }


}
