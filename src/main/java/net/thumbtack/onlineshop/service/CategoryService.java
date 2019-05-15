package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.daoImpl.CategoryDaoImpl;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.dto.requests.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.requests.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.responses.AddCategoryResponse;
import net.thumbtack.onlineshop.model.*;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    public AddCategoryResponse addCategory(AddCategoryRequest request, String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
        Category category = new Category(0, request.getName(), request.getParentId());
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        return createAddCategoryResponse(addedCategory);
    }

    public AddCategoryResponse getCategory(String number, String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
        Integer id = getCategoryIdFromAddressLine(number);
        Category category = categoryDao.getCategory(id);
        return createAddCategoryResponse(category);
    }

    public AddCategoryResponse editCategory(EditCategoryRequest request, String number, String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
        Integer id = getCategoryIdFromAddressLine(number);
        Category oldCategory = categoryDao.getCategory(id);
        Category newCategory = new Category(
                id,
                request.getName()==null?oldCategory.getName():request.getName(),
                request.getParentId()==null?oldCategory.getParentId():request.getParentId());
        categoryDao.editCategory(newCategory);
        Category editedCategory = categoryDao.getCategory(newCategory.getId());
        return createAddCategoryResponse(editedCategory);
    }

    public String deleteCategory(String number, String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
        Integer id = getCategoryIdFromAddressLine(number);
        categoryDao.deleteCategory(id);
        return "{}";
    }

    public List<AddCategoryResponse> getAllCategories(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
        List<Category> categories = categoryDao.getAllCategories();
        return createAddCategoryResponses(categories);
    }

    private Integer getCategoryIdFromAddressLine(String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                    "number of category in address line",
                    "Use whole numbers after {api/categories/} ");
        }
        return id;
    }

    private AddCategoryResponse createAddCategoryResponse(Category addedCategory) {
        return new AddCategoryResponse(
                addedCategory.getId(),
                addedCategory.getName(),
                addedCategory.getParentId(),
                addedCategory.getParentName());
    }

    private List<AddCategoryResponse> createAddCategoryResponses(List<Category> categories) {
        List<AddCategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(createAddCategoryResponse(category));
            if (category.getChildCategories() != null && category.getChildCategories().size() != 0) {
                for (Category childCategory : category.getChildCategories()) {
                    responses.add(createAddCategoryResponse(childCategory));
                }
            }
        }
        return responses;
    }


}
