package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;

import java.util.List;

public interface ProductDao {

    void addProduct(String cookieValue, Product product, List<Integer> categoriesId) throws OnlineShopException;

    void editProduct(String cookieValue, Product product, List<Integer> categoriesId) throws OnlineShopException;

    void deleteProduct(String cookieValue, Integer id) throws OnlineShopException;

    Product getProduct(String cookieValue, Integer id) throws OnlineShopException;

    List<Product> getProductsByCategory(String cookieValue, List<Integer> categories, String order) throws OnlineShopException;

    List<Product> addProductInBasket(String cookieValue, Product basketProduct) throws OnlineShopException;

    void deleteProductFromBasket(String cookieValue, Integer productId) throws OnlineShopException;
}
