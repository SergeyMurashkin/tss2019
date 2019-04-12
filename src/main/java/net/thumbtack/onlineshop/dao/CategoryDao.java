package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.OnlineShopException;

import java.util.List;

public interface CategoryDao {

    Category addCategory(String cookieValue, Category category) throws OnlineShopException;

    Category getCategory(String cookieValue, Integer id) throws OnlineShopException;

    Category editCategory(String cookieValue, Category category) throws OnlineShopException;

    void deleteCategory(String cookieValue, Integer id);

    List<Category> getAllCategories(String cookieValue);

}
