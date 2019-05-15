package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;

import java.util.List;

public interface BasketDao {

    void addProductInBasket(Client client, Product productToBasket);

    List<Product> getClientBasket(Client client);

    Product getBasketProduct(Client client, Integer productId) throws OnlineShopException;

    void deleteProductFromBasket(Client client, Integer productId);

    void changeBasketProductQuantity(Client client, Product newBasketProduct);

}
