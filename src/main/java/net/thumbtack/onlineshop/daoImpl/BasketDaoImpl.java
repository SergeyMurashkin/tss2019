package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.BasketDao;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BasketDaoImpl extends DaoImplBase implements BasketDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasketDaoImpl.class);

    @Override
    public void addProductInBasket(Client client, Product productToBasket) {
        LOGGER.debug("DAO add Product {} in basket", productToBasket);
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).addProductInBasket(client, productToBasket);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't add Product. {}", ex);
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
                LOGGER.error("Can't get client basket. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Product getBasketProduct(Client client, Integer productId) throws OnlineShopException {
        LOGGER.debug("DAO get client basket product.");
        try (SqlSession sqlSession = getSession()) {
            try {
                Product basketProduct = getBasketMapper(sqlSession).getBasketProduct(client, productId);
                if (basketProduct == null) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                            "id",
                            OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());
                }
                return basketProduct;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get client basket product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    public void deleteProductFromBasket(Client client, Integer productId) {
        LOGGER.debug("DAO delete Product from Basket with productId {}", productId);
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).deleteProductFromBasket(client.getId(), productId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't delete Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public void changeBasketProductQuantity(Client client, Product newBasketProduct) {
        LOGGER.debug("DAO change product quantity {}", newBasketProduct);
        try (SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).changeProductQuantity(client, newBasketProduct);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't change product quantity. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

}
