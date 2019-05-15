package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;

import java.util.List;

public interface ProductDao {

    void addProduct(Product product, List<Integer> categoriesId) throws OnlineShopException;

    Product getProduct(Integer id) throws OnlineShopException;

    void editProduct(Product product,
                     List<Integer> categoriesId) throws OnlineShopException;

    List<Product> getAllProductsByProductOrder();

    List<Product> getAllProductsByCategoryOrder();

    List<Product> getProductsWithoutCategories();

    List<Product> getProductsByCategoryOrder(List<Integer> categoriesId);

    List<Product> getProductsByProductOrder(List<Integer> categoriesId);

    void deleteProduct(Integer id);

    List<Integer> getAllProductsId();

    List<Integer> getProductsIdByCategories(List<Integer> categoriesId);

    List<Integer> getProductsIdByCategoriesAndWithout(List<Integer> categoriesId);
}
