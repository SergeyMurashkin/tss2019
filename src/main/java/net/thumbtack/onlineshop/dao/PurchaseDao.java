package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;
import net.thumbtack.onlineshop.model.Purchase;

import java.util.List;

public interface PurchaseDao {

    void purchaseProduct(Client client, Product product, Purchase purchase) throws OnlineShopException;

    List<Product> purchaseProductsFromBasket(Client client, List<Product> products);

    List<Purchase> getPurchasesByWithoutCategoriesNoLimit();

    List<Purchase> getPurchasesByWithoutCategoriesLimitProducts(List<Integer> productsId);

    List<Purchase> getPurchasesByWithoutCategoriesLimitClients(List<Integer> clientsId);

    List<Purchase> getPurchasesByWithoutCategoriesLimitProductsAndClients(List<Integer> productsId, List<Integer> clientsId);

    List<Purchase> getPurchasesByCategoryNoLimit(int categoryId);

    List<Purchase> getPurchasesByCategoryLimitProducts(int categoryId, List<Integer> productsId);

    List<Purchase> getPurchasesByCategoryLimitClients(int categoryId, List<Integer> clientsId);

    List<Purchase> getPurchasesByCategoryLimitProductsAndClients(int categoryId, List<Integer> productsId, List<Integer> clientsId);

    List<Purchase> getPurchasesByProductNoLimit(int productId);

    List<Purchase> getPurchasesByProductLimitClients(int productId, List<Integer> clientsId);

    List<Purchase> getPurchasesByClientNoLimit(int clientId);

    List<Purchase> getPurchasesByClientLimitProducts(int clientId, List<Integer> productsId);
}
