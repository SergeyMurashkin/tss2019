package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.PurchaseDao;
import net.thumbtack.onlineshop.models.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PurchaseDaoImpl extends DaoImplBase implements PurchaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseDaoImpl.class);

    @Override
    public void purchaseProduct(String cookieValue, Purchase purchase) {
        LOGGER.debug("DAO purchase Product {}");
        Client client;
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.CLIENT.name())) {
                try {
                    client = getUserMapper(sqlSession).getClient(user);
                    Product product = getProductMapper(sqlSession).getProduct(purchase.getId());
                    purchase.setUserId(client.getId());
                    if(product.getCount()>=purchase.getCount() &&
                            client.getDeposit()>=(purchase.getCount()*purchase.getPrice()) &&
                            product.getName().equals(purchase.getName()) &&
                            product.getPrice().equals(purchase.getPrice())){
                        getPurchaseMapper(sqlSession).addPurchase(purchase);
                        getUserMapper(sqlSession).spendMoney(client, purchase.getCount()*purchase.getPrice());
                        getProductMapper(sqlSession).reduceProductCount(product, purchase.getCount());
                    }

                } catch (RuntimeException ex) {
                    LOGGER.info("Can't purchase Product. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();

            }
        }
    }


}
