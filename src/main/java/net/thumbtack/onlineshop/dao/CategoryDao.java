package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.models.Category;

import java.util.List;

public interface CategoryDao {

    Category addCategory(String cookieValue, Category category);

    Category getCategory(String cookieValue, Integer id);

    Category editCategory(String cookieValue, Category category);

    void deleteCategory(String cookieValue, Integer id);

    List<Category> getAllCategories(String cookieValue);

}
