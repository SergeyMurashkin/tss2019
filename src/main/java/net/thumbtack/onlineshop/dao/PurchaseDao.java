package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.dto.responses.ConsolidatedStatementResponse;
import net.thumbtack.onlineshop.dto.responses.PurchaseProductFromBasketResponse;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;
import net.thumbtack.onlineshop.model.Purchase;

import java.util.List;

public interface PurchaseDao {

    void purchaseProduct(Client client, Product product, Purchase purchase);

    List<Product> purchaseProductsFromBasket(Client client,
                                             List<Product> products,
                                             Integer totalCost);

    ConsolidatedStatementResponse getConsolidatedStatement(List<Integer> categoriesId,
                                                           List<Integer> productsId,
                                                           List<Integer> clientsId)  throws OnlineShopException;
}
