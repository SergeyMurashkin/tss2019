package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.PurchaseDao;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDaoImpl extends DaoImplBase implements PurchaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseDaoImpl.class);

    @Override
    public void purchaseProduct(Client client, Product product, Purchase purchase) throws OnlineShopException {
        LOGGER.debug("DAO purchase Product {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                if (getProductMapper(sqlSession).reduceProductCount(product, purchase.getCount()) != 1) {
                    LOGGER.error("Can't reduce product count.");
                    sqlSession.rollback();
                    throw new OnlineShopException(
                            OnlineShopErrorCode.PRODUCT_STATE_CHANGING,
                            null,
                            OnlineShopErrorCode.PRODUCT_STATE_CHANGING.getErrorText());
                }
                if (getDepositMapper(sqlSession).chargeMoney(client.getDeposit(), purchase.getCount() * purchase.getPrice()) != 1) {
                    LOGGER.error("Can't charge money.");
                    sqlSession.rollback();
                    throw new OnlineShopException(
                            OnlineShopErrorCode.DEPOSIT_STATE_CHANGING,
                            null,
                            OnlineShopErrorCode.DEPOSIT_STATE_CHANGING.getErrorText());
                }
                if (getPurchaseMapper(sqlSession).addPurchase(purchase) != 1) {
                    LOGGER.error("Can't purchase Product.");
                    sqlSession.rollback();
                    throw new OnlineShopException(
                            OnlineShopErrorCode.PURCHASE_NOT_ADDED,
                            null,
                            OnlineShopErrorCode.PURCHASE_NOT_ADDED.getErrorText());
                }
            } catch (RuntimeException ex) {
                LOGGER.error("Can't purchase Product. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Product> purchaseProductsFromBasket(Client client,
                                                    List<Product> products) {
        LOGGER.debug("DAO purchase basket products {}");
        List<Product> bought = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            try {
                int totalCost = 0;
                for (Product boughtProduct : products) {

                    Savepoint savepoint;
                    try {
                        savepoint = sqlSession.getConnection().setSavepoint("savepoint");
                    } catch (SQLException e) {
                        LOGGER.error("Can't create savepoint. {}", e);
                        sqlSession.rollback();
                        break;
                    }

                    Purchase purchase = new Purchase(0,
                            client.getId(),
                            boughtProduct.getId(),
                            boughtProduct.getName(),
                            boughtProduct.getPrice(),
                            boughtProduct.getCount());

                    if (getProductMapper(sqlSession).reduceProductCount(boughtProduct, boughtProduct.getCount()) == 1 &&
                            getBasketMapper(sqlSession).deleteProductFromBasket(client.getId(), boughtProduct.getId()) == 1 &&
                            getPurchaseMapper(sqlSession).addPurchase(purchase) == 1) {
                        bought.add(boughtProduct);
                        totalCost += boughtProduct.getCount() * boughtProduct.getPrice();
                    } else {
                        try {
                            sqlSession.getConnection().rollback(savepoint);
                        } catch (SQLException e) {
                            LOGGER.error("Can't load savepoint. {}", e);
                            sqlSession.rollback();
                            break;
                        }
                    }
                }
                if (totalCost != 0) {
                    getDepositMapper(sqlSession).chargeMoney(client.getDeposit(), totalCost);
                }

            } catch (RuntimeException ex) {
                LOGGER.error("Can't purchase basket products. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return bought;
    }

    @Override
    public List<Purchase> getPurchasesByWithoutCategoriesNoLimit() {
        LOGGER.debug("DAO get purchases of products without categories.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByWithoutCategoryNoLimit();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products without categories. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByWithoutCategoriesLimitProducts(List<Integer> productsId) {
        LOGGER.debug("DAO get purchases of products without categories.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByWithoutCategoryLimitProducts(productsId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products without categories. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByWithoutCategoriesLimitClients(List<Integer> clientsId) {
        LOGGER.debug("DAO get purchases of products without categories.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByWithoutCategoryLimitClients(clientsId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products without categories. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByWithoutCategoriesLimitProductsAndClients(List<Integer> productsId, List<Integer> clientsId) {
        LOGGER.debug("DAO get purchases of products without categories.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByWithoutCategoryLimitProductsAndClients(productsId, clientsId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products without categories. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByCategoryNoLimit(int categoryId) {
        LOGGER.debug("DAO get purchases of products by categoryId.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByCategoryNoLimit(categoryId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products by categoryId. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByCategoryLimitProducts(int categoryId, List<Integer> productsId) {
        LOGGER.debug("DAO get purchases of products by categoryId.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByCategoryLimitProducts(categoryId, productsId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products by categoryId. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByCategoryLimitClients(int categoryId, List<Integer> clientsId) {
        LOGGER.debug("DAO get purchases of products by categoryId.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByCategoryLimitClients(categoryId, clientsId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products by categoryId. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByCategoryLimitProductsAndClients(int categoryId, List<Integer> productsId, List<Integer> clientsId) {
        LOGGER.debug("DAO get purchases of products by categoryId.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByCategoryLimitProductsAndClients(categoryId,productsId,clientsId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products by categoryId. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByProductNoLimit(int productId) {
        LOGGER.debug("DAO get purchases of products by productId.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByProductNoLimit(productId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products by productId. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }


    @Override
    public List<Purchase> getPurchasesByProductLimitClients(int productId, List<Integer> clientsId) {
        LOGGER.debug("DAO get purchases of products by productId.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByProductLimitClients(productId, clientsId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products by productId. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByClientNoLimit(int clientId) {
        LOGGER.debug("DAO get purchases of products by clientId.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByClientNoLimit(clientId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products by clientId. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByClientLimitProducts(int clientId, List<Integer> productsId) {
        LOGGER.debug("DAO get purchases of products by clientId.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getPurchaseMapper(sqlSession).getPurchasesByClientLimitProducts(clientId,productsId);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get purchases of products by clientId. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }


}
