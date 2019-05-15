package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.OnlineShopException;

import java.util.List;

public interface CategoryDao {

    Category getCategory(Integer id) throws OnlineShopException;

    void addCategory(Category category) throws OnlineShopException;

    void editCategory(Category category) throws OnlineShopException;

    void deleteCategory(Integer id);

    List<Category> getAllCategories();

    List<Integer> getAllCategoriesAndSubCategoriesId();
}
