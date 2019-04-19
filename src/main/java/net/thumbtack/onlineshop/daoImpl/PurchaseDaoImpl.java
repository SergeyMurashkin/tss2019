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
    public void purchaseProduct(String cookieValue, Purchase purchase) throws OnlineShopException {
        LOGGER.debug("DAO purchase Product {}");

        try (SqlSession sqlSession = getSession()) {
            try {
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
                Client client = getUserMapper(sqlSession).getClient(user);
                purchase.setClientId(client.getId());
                Product product = getProductMapper(sqlSession).getProduct(purchase.getProductId());
                if (product == null) {
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

    @Override
    public PurchaseProductFromBasketResponse purchaseProductsFromBasket(String cookieValue,
                                                                        List<Product> products) throws OnlineShopException {
        LOGGER.debug("DAO purchase basket products {}");
        List<Product> bought = new ArrayList<>();
        List<Product> remaining;
        try (SqlSession sqlSession = getSession()) {
            try {
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

                List<Product> basketProducts = getBasketMapper(sqlSession).getClientBasket(user);
                for (Product product : products) {
                    int index = basketProducts.indexOf(product);
                    if (index != -1) {
                        Product basketProduct = basketProducts.get(index);
                        if (product.getCount() == 0 || product.getCount() > basketProduct.getCount()) {
                            product.setCount(basketProduct.getCount());
                        }
                        Product marketProduct = getProductMapper(sqlSession).getProduct(product.getId());
                        if(product.equals(marketProduct) && marketProduct.getCount()>=product.getCount()){
                            bought.add(product);
                        }
                    }
                }

                int totalCost=0;
                for (Product boughtProduct : bought) {
                    totalCost+=boughtProduct.getCount()*boughtProduct.getPrice();
                }

                Client client = getUserMapper(sqlSession).getClient(user);

                if(totalCost<=client.getDeposit().getDeposit()){
                    for (Product boughtProduct : bought){
                        Purchase purchase = new Purchase(0,
                                client.getId(),
                                boughtProduct.getId(),
                                boughtProduct.getName(),
                                boughtProduct.getPrice(),
                                boughtProduct.getCount());
                        getPurchaseMapper(sqlSession).addPurchase(purchase);
                        getProductMapper(sqlSession).reduceProductCount(boughtProduct,boughtProduct.getCount());
                        getBasketMapper(sqlSession).deleteProductFromBasket(client.getId(),boughtProduct.getId());
                    }
                    getDepositMapper(sqlSession).spendMoney(client,totalCost);
                    remaining = getBasketMapper(sqlSession).getClientBasket(client);
                }else{
                    remaining = getBasketMapper(sqlSession).getClientBasket(client);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't purchase basket products. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return new PurchaseProductFromBasketResponse(bought,remaining);
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
