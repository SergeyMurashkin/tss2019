package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.models.Product;
import net.thumbtack.onlineshop.models.User;
import net.thumbtack.onlineshop.models.UserType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl extends DaoImplBase implements ProductDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

    public void addProduct(String cookieValue, Product product) {
        LOGGER.debug("DAO add Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    getProductMapper(sqlSession).addProduct(product);
                    if( ! product.getCategories().isEmpty() ) {
                        getProductMapper(sqlSession).addProductCategories(product.getCategories(), product);
                    }
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't add Product. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();

            }
        }
    }


    public void editProduct(String cookieValue, Product product) {
        LOGGER.debug("DAO edit Product {}", product);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    getProductMapper(sqlSession).editProduct(product);
                    getProductMapper(sqlSession).deleteAllProductCategories(product);
                    getProductMapper(sqlSession).addProductCategories(product.getCategories(), product);
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't edit Product. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();

            }
        }
    }


    public void deleteProduct(String cookieValue, Integer id) {
        LOGGER.debug("DAO delete Product with id {}", id);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())) {
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
    }


    public Product getProduct(String cookieValue, Integer id) {
        LOGGER.debug("DAO get Product with id {}", id);
        Product product = new Product();
        try (SqlSession sqlSession = getSession()) {
            //change check at EXISTS
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())
            || user.getUserType().equals(UserType.CLIENT.name())) {
                try {
                    product = getProductMapper(sqlSession).getProduct(id);
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't get Product. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();
            }
        }
        return product;
    }


    //all need to change
    @Override
    public List<Product> getProductsByCategory(String cookieValue, List<Integer> categories, String order) {
        LOGGER.debug("DAO get Products");
        List<Product> products = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            //change check at EXISTS
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())
                    || user.getUserType().equals(UserType.CLIENT.name())) {
                try {
                    if(categories==null){
                        products.addAll(getProductMapper(sqlSession).getAllProducts());
                        return products;
                    }
                    if(categories.size()==0){
                        return getProductMapper(sqlSession).getProductsWithoutCategories();
                    } else {
                        if(order.equals("product")){
                            return getProductMapper(sqlSession).getProductsWithCategories(categories);
                        }
                        if(order.equals("category")){

                            return getProductMapper(sqlSession).getProductsWithCategories(categories);
                        }
                    }
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't get Products. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
            }
        }
        return products;
    }


}
