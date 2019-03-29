package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.models.Category;
import net.thumbtack.onlineshop.models.Product;

import java.util.List;

public interface ProductDao {

    void addProduct(String cookieValue, Product product);

    void editProduct(String cookieValue, Product product);

    void deleteProduct(String cookieValue, Integer id);

    Product getProduct(String cookieValue, Integer id);

    List<Product> getProductsByCategory(String cookieValue, List<Integer> categories, String order);

}
