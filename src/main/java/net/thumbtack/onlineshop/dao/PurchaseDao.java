package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.dto.responses.ConsolidatedStatementResponse;
import net.thumbtack.onlineshop.dto.responses.PurchaseProductFromBasketResponse;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;
import net.thumbtack.onlineshop.model.Purchase;

import java.util.List;

public interface PurchaseDao {

    void purchaseProduct(String cookieValue, Purchase purchase) throws OnlineShopException;

    PurchaseProductFromBasketResponse purchaseProductsFromBasket(String cookieValue,
                                                                 List<Product> products)  throws OnlineShopException;

    ConsolidatedStatementResponse getConsolidatedStatement(List<Integer> categoriesId,
                                                           List<Integer> productsId,
                                                           List<Integer> clientsId)  throws OnlineShopException;
}
