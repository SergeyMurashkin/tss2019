package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.PurchaseDao;
import net.thumbtack.onlineshop.dto.responses.ConsolidatedStatementResponse;
import net.thumbtack.onlineshop.dto.responses.PurchaseProductFromBasketResponse;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDaoImpl extends DaoImplBase implements PurchaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseDaoImpl.class);

    @Override
    public void purchaseProduct(Client client, Product product, Purchase purchase) {
        LOGGER.debug("DAO purchase Product {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).reduceProductCount(product, purchase.getCount());
                getDepositMapper(sqlSession).chargeMoney(client.getDeposit(), purchase.getCount() * purchase.getPrice());
                getPurchaseMapper(sqlSession).addPurchase(purchase);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't purchase Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Product> purchaseProductsFromBasket(Client client,
                                                    List<Product> products,
                                                    Integer totalCost)  {
        LOGGER.debug("DAO purchase basket products {}");
        List<Product> bought = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                if(totalCost<=client.getDeposit().getDeposit()) {
                    for (Product boughtProduct : products) {
                        Purchase purchase = new Purchase(0,
                                client.getId(),
                                boughtProduct.getId(),
                                boughtProduct.getName(),
                                boughtProduct.getPrice(),
                                boughtProduct.getCount());
                        System.out.println(purchase);
                        System.out.println(boughtProduct);

                        if (getProductMapper(sqlSession).reduceProductCount(boughtProduct, boughtProduct.getCount()) == 1) {
                            getBasketMapper(sqlSession).deleteProductFromBasket(client.getId(), boughtProduct.getId());
                            getPurchaseMapper(sqlSession).addPurchase(purchase);
                            bought.add(boughtProduct);
                        } else {
                            totalCost -= boughtProduct.getCount() * boughtProduct.getPrice();
                        }
                    }
                    getDepositMapper(sqlSession).chargeMoney(client.getDeposit(), totalCost);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't purchase basket products. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return bought;
    }








    @Override
    public ConsolidatedStatementResponse getConsolidatedStatement(List<Integer> categoriesId,
                                                                  List<Integer> productsId,
                                                                  List<Integer> clientsId) throws OnlineShopException {
        LOGGER.debug("DAO get consolidated statement");
        List<Product> products;
        try (SqlSession sqlSession = getSession()) {
            try {
                if(categoriesId==null){
                    // all
                }else{
                    if(categoriesId.size() == 1&&categoriesId.get(0)==0){
                        //noting
                    }else{
                        //by list
                    }
                }
                if(productsId==null){
                    // all
                }else{
                    if(productsId.size() == 1&&productsId.get(0)==0){
                        //noting
                    }else{
                        //by list
                    }
                }

                if(clientsId==null){
                    //all
                }else{
                    if(clientsId.size() == 1&&clientsId.get(0)==0){
                        //noting
                    }else{
                        //by list
                    }
                }


            } catch (RuntimeException ex) {
                LOGGER.info("Can't get consolidated statement. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return null;
    }


}
