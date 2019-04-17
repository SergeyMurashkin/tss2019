package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.PurchaseDao;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PurchaseDaoImpl extends DaoImplBase implements PurchaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseDaoImpl.class);

    @Override
    public void purchaseProduct(String cookieValue, Purchase purchase) throws OnlineShopException {
        LOGGER.debug("DAO purchase Product {}");
        Client client;
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
                client = getUserMapper(sqlSession).getClient(user);
                purchase.setClientId(client.getId());
                Product product = getProductMapper(sqlSession).getProduct(purchase.getProductId());
                if(product==null){
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                            "id",
                            OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());
                }
                if (!product.getName().equals(purchase.getName())) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                            "name",
                            OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + product.getName());
                }
                if (product.getPrice() != (purchase.getPrice())) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                            "price",
                            OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + product.getPrice());
                }
                if (product.getCount() < purchase.getCount()) {
                    throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_INSUFFICIENT_AMOUNT,
                            "count",
                            OnlineShopErrorCode.PRODUCT_INSUFFICIENT_AMOUNT.getErrorText() + product.getCount());
                }
                if (client.getDeposit().getDeposit() < (purchase.getCount() * purchase.getPrice())) {
                    throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_INSUFFICIENT_AMOUNT,
                            null,
                            OnlineShopErrorCode.DEPOSIT_INSUFFICIENT_AMOUNT.getErrorText());
                }

                getPurchaseMapper(sqlSession).addPurchase(purchase);
                getDepositMapper(sqlSession).spendMoney(client, purchase.getCount() * purchase.getPrice());
                getProductMapper(sqlSession).reduceProductCount(product, purchase.getCount());
            } catch (RuntimeException ex) {
                LOGGER.info("Can't purchase Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }


}
