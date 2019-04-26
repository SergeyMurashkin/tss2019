package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.OnlineShopException;

import java.util.List;

public interface CategoryDao {

    boolean isCategoryNameExists(String name);

    Category getCategory(Integer id);

    void addCategory(Category category);

    void editCategory(Category category);

    void deleteCategory(Integer id);

    List<Category> getAllCategories();

    List<Integer> checkCategories(List<Integer> categoriesId);
}
