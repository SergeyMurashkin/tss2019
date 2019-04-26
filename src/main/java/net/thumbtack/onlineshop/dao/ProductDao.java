package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;

import java.util.List;

public interface ProductDao {

    void addProduct(Product product, List<Integer> categoriesId);

    Product getProduct(Integer id);

    void editProduct(Product product, List<Integer> categoriesId) throws OnlineShopException;

    void deleteProduct(Integer id);

    List<Product> getProductsByCategory(List<Integer> categories, String order);

    boolean checkIsProductInClientBasket(Client client, Product productToBasket);

    void addProductInBasket(Client client, Product productToBasket);

    List<Product> getClientBasket(Client client);

    Product getBasketProduct(Client client, Product product);

    void deleteProductFromBasket(Client client, Integer productId);

    List<Product> changeBasketProductQuantity(Client client, Product newBasketProduct);

}
