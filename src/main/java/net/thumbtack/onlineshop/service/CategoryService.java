package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.daoImpl.CategoryDaoImpl;
import net.thumbtack.onlineshop.dto.requests.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.requests.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.responses.AddCategoryResponse;
import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    public AddCategoryResponse addCategory(AddCategoryRequest request) throws OnlineShopException {
        if (categoryDao.isCategoryNameExists(request.getName())){
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE,
                    "name",
                    OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE.getErrorText());
        }
        Category category = new Category(0, request.getName(), request.getParentId());
        if (category.getParentId() == 0) {
            category.setParentId(null);
        } else {
            Category parentCategory = categoryDao.getCategory(category.getParentId());
            if (parentCategory == null) {
                throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                        "parentId",
                        OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText());
            }
            category.setParentName(parentCategory.getName());
        }
        categoryDao.addCategory(category);
        return createAddCategoryResponse(category);
    }

    public AddCategoryResponse getCategory(String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                    "number of category in address line",
                    "Use numbers after {api/categories/} ");
        }
        Category category = categoryDao.getCategory(id);
        if (category == null) {
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                    "number of category in address line",
                    OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText());
        }
        return createAddCategoryResponse(category);
    }

    private AddCategoryResponse createAddCategoryResponse(Category addedCategory) {
        return new AddCategoryResponse(
                addedCategory.getId(),
                addedCategory.getName(),
                addedCategory.getParentId(),
                addedCategory.getParentName());
    }

    public AddCategoryResponse editCategory(EditCategoryRequest request, String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                    "number of category in address line",
                    "Use numbers after {api/categories/} ");
        }
        Category oldCategory = categoryDao.getCategory(id);
        Category newCategory = new Category(id, request.getName(), request.getParentId());
        if (newCategory.getParentId() == 0) {
            newCategory.setParentId(null);
        }

        if(!newCategory.getName().equalsIgnoreCase(oldCategory.getName())){
            if (categoryDao.isCategoryNameExists(newCategory.getName())){
                throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE,
                        "name",
                        OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE.getErrorText());
            }
        }

        if (oldCategory.getParentId()==null && newCategory.getParentId()!=null ||
        oldCategory.getParentId()!=null && newCategory.getParentId()==null){
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_HIERARCHY_VIOLATION,
                    "parentId",
                    OnlineShopErrorCode.CATEGORY_HIERARCHY_VIOLATION.getErrorText());
        }

        if (newCategory.getParentId()!=null) {
            Category parentCategory = categoryDao.getCategory(newCategory.getParentId());
            if (parentCategory == null) {
                throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                        "parentId",
                        OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText());
            }
            newCategory.setParentName(parentCategory.getName());
        }
        categoryDao.editCategory(newCategory);
        return createAddCategoryResponse(newCategory);
    }

    public String deleteCategory(String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                    "number of category in address line",
                    "Use numbers after {api/categories/} ");
        }
        categoryDao.deleteCategory(id);
        return "{}";
    }

    public List<AddCategoryResponse> getAllCategories() {
        List<Category> categories = categoryDao.getAllCategories();
        List<AddCategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(createAddCategoryResponse(category));
            if(category.getChildCategories()!=null && category.getChildCategories().size()!=0){
                for (Category childCategory : category.getChildCategories()) {
                responses.add(createAddCategoryResponse(childCategory));
                }
            }
        }
        return responses;
    }

    public void checkCategories(List<Integer> categoriesId) throws OnlineShopException {
        if(categoriesId!=null && !categoriesId.isEmpty()) {
            List<Integer> realCategoriesId = categoryDao.checkCategories(categoriesId);
            if (realCategoriesId.size() != categoriesId.size()
                    || !realCategoriesId.containsAll(categoriesId)) {
                categoriesId.removeAll(realCategoriesId);
                throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                        "categoriesId",
                        OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText() + " Nonexistent categories: " + categoriesId);
            }
        }
    }
}
