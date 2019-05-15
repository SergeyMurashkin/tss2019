package net.thumbtack.onlineshop.testDao;

import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.OnlineShopException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCategoryDao extends TestBaseDao {


    @Test(expected = OnlineShopException.class)
    public void testNonexistenceCategoryGetting() throws OnlineShopException {
        categoryDao.getCategory(0);
    }

    @Test()
    public void testCategoryGetting() throws OnlineShopException {
        Category category = new Category(0,"одежда",null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        Assert.assertNotEquals(0, addedCategory.getId());
        Assert.assertEquals("одежда",addedCategory.getName());
        Assert.assertNull(addedCategory.getParentId());
        Assert.assertNull(addedCategory.getParentName());
        Assert.assertEquals(0,addedCategory.getChildCategories().size());
    }

    @Test
    public void testCategoryAdding() throws OnlineShopException {
        Category category = new Category(0,"одежда",null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        Assert.assertNotEquals(0, addedCategory.getId());
        Assert.assertEquals("одежда",addedCategory.getName());
        Assert.assertNull(addedCategory.getParentId());
        Assert.assertNull(addedCategory.getParentName());
        Assert.assertEquals(0,addedCategory.getChildCategories().size());

        Category category2 = new Category(0,"мужская одежда", category.getId());
        categoryDao.addCategory(category2);
        Category addedCategory2 = categoryDao.getCategory(category2.getId());
        Assert.assertNotEquals(0, addedCategory2.getId());
        Assert.assertEquals("мужская одежда",addedCategory2.getName());
        Assert.assertEquals(addedCategory2.getParentId(),(Integer) addedCategory.getId());
        Assert.assertEquals(addedCategory2.getParentName(),addedCategory.getName());
        Assert.assertNull(addedCategory2.getChildCategories());

        addedCategory = categoryDao.getCategory(category.getId());
        Assert.assertNotEquals(0, addedCategory.getId());
        Assert.assertEquals("одежда",addedCategory.getName());
        Assert.assertNull(addedCategory.getParentId());
        Assert.assertNull(addedCategory.getParentName());
        Assert.assertEquals(1,addedCategory.getChildCategories().size());
        Assert.assertEquals(addedCategory2, addedCategory.getChildCategories().get(0));
    }

    @Test
    public void testCategoryAddingWithDuplicateName() throws OnlineShopException {
        String categoryName = "одежда";

        Category category = new Category(0,categoryName,null);
        categoryDao.addCategory(category);

        Category category2 = new Category(0,categoryName,null);
        OnlineShopException e = null;
        try{
            categoryDao.addCategory(category2);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("name",e.getField());
    }

    @Test
    public void testCategoryAddingWithNonexistentParentId() throws OnlineShopException {
        Category category = new Category(0,"одежда", null);
        categoryDao.addCategory(category);

        Category category2 = new Category(0,"мужская одежда", 0);
        OnlineShopException e = null;
        try{
            categoryDao.addCategory(category2);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("parentId",e.getField());
    }

    @Test
    public void testCategoryEditing() throws OnlineShopException {
        Category category = new Category(0,"одежда 2018",  null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        Assert.assertNotEquals(0, addedCategory.getId());
        Assert.assertEquals("одежда 2018",addedCategory.getName());
        Assert.assertNull(addedCategory.getParentId());
        Assert.assertNull(addedCategory.getParentName());
        Assert.assertEquals(0,addedCategory.getChildCategories().size());

        Category category2 = new Category(0,"мужская одежда", addedCategory.getId());
        categoryDao.addCategory(category2);
        Category subCategory = categoryDao.getCategory(category2.getId());
        Assert.assertNotEquals(0, subCategory.getId());
        Assert.assertEquals(subCategory.getParentId(),(Integer) addedCategory.getId());
        Assert.assertEquals(subCategory.getParentName(),addedCategory.getName());
        Assert.assertNull(subCategory.getChildCategories());

        Category newCategory = new Category(addedCategory.getId(),"одежда 2019", null);
        categoryDao.editCategory(newCategory);
        Category editedCategory = categoryDao.getCategory(addedCategory.getId());
        Assert.assertNotEquals(0, editedCategory.getId());
        Assert.assertEquals("одежда 2019",editedCategory.getName());
        Assert.assertNull(editedCategory.getParentId());
        Assert.assertNull(editedCategory.getParentName());
        Assert.assertEquals(1,editedCategory.getChildCategories().size());

        subCategory = categoryDao.getCategory(category2.getId());
        Assert.assertNotEquals(0, subCategory.getId());
        Assert.assertEquals(subCategory.getParentId(),(Integer) addedCategory.getId());
        Assert.assertEquals(subCategory.getParentName(),editedCategory.getName());
        Assert.assertNull(subCategory.getChildCategories());
    }

    @Test
    public void testCategoryEditingWithDuplicateName() throws OnlineShopException {

        Category category = new Category(0,"одежда 2018", null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        Assert.assertNotEquals(0, addedCategory.getId());
        Assert.assertEquals("одежда 2018",addedCategory.getName());
        Assert.assertNull(addedCategory.getParentId());
        Assert.assertNull(addedCategory.getParentName());
        Assert.assertEquals(0,addedCategory.getChildCategories().size());

        Category category2 = new Category(0,"одежда 2019", null);
        categoryDao.addCategory(category2);
        Category addedCategory2 = categoryDao.getCategory(category2.getId());
        Assert.assertNotEquals(0, addedCategory2.getId());
        Assert.assertEquals("одежда 2019",addedCategory2.getName());
        Assert.assertNull(addedCategory2.getParentId());
        Assert.assertNull(addedCategory2.getParentName());
        Assert.assertEquals(0,addedCategory2.getChildCategories().size());

        Category editedCategory = new Category(addedCategory.getId(),"одежда 2019", null);
        OnlineShopException e = null;
        try{
            categoryDao.editCategory(editedCategory);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("name",e.getField());
    }

    @Test
    public void testCategoryEditingWithNonexistentParentId() throws OnlineShopException {

        Category category = new Category(0,"одежда 2018",null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        Assert.assertNotEquals(0, addedCategory.getId());
        Assert.assertEquals("одежда 2018",addedCategory.getName());
        Assert.assertNull(addedCategory.getParentId());
        Assert.assertNull(addedCategory.getParentName());
        Assert.assertEquals(0,addedCategory.getChildCategories().size());

        Category subCategory = new Category(0,"мужская одежда", addedCategory.getId());
        categoryDao.addCategory(subCategory);
        Category addedSubCategory = categoryDao.getCategory(subCategory.getId());
        Assert.assertNotEquals(0, addedSubCategory.getId());
        Assert.assertEquals("мужская одежда",addedSubCategory.getName());
        Assert.assertEquals(addedSubCategory.getParentId(),(Integer) addedCategory.getId());
        Assert.assertEquals(addedSubCategory.getParentName(), addedCategory.getName());
        Assert.assertNull(addedSubCategory.getChildCategories());

        Category editedSubCategory = new Category(addedSubCategory.getId(),addedSubCategory.getName(), 0);
        OnlineShopException e = null;
        try{
            categoryDao.editCategory(editedSubCategory);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("parentId",e.getField());
    }


    @Test
    public void testCategoryDeletion() throws OnlineShopException {
        Category category = new Category(0,"одежда 2018",  null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        Assert.assertNotNull(addedCategory);

        Category category2 = new Category(0,"одежда 2019",  null);
        categoryDao.addCategory(category2);
        Category addedCategory2 = categoryDao.getCategory(category2.getId());
        Assert.assertNotNull(addedCategory2);

        categoryDao.deleteCategory(addedCategory.getId());

        OnlineShopException e = null;
        try{
            categoryDao.getCategory(addedCategory.getId());
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);

        categoryDao.getCategory(addedCategory2.getId());
    }

    @Test
    public void testCategoryWithChildDeletion() throws OnlineShopException {
        Category category = new Category(0,"одежда 2018",  null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        Assert.assertNotNull(addedCategory);

        Category subCategory = new Category(0,"мужская одежда", addedCategory.getId());
        categoryDao.addCategory(subCategory);
        Category addedSubCategory = categoryDao.getCategory(subCategory.getId());
        Assert.assertNotNull(addedSubCategory);

        categoryDao.deleteCategory(addedCategory.getId());

        OnlineShopException e = null;
        try{
            categoryDao.getCategory(addedCategory.getId());
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);

        OnlineShopException e2 = null;
        try{
            categoryDao.getCategory(addedSubCategory.getId());
        } catch (OnlineShopException ex){
            e2=ex;
        }
        Assert.assertNotNull(e2);
    }


    @Test
    public void testAllCategoriesGetting() throws OnlineShopException {
        Category category = new Category(0,"одежда 2019",  null);
        categoryDao.addCategory(category);

        Category subCategory = new Category(0,"мужская одежда", category.getId());
        categoryDao.addCategory(subCategory);

        Category category2 = new Category(0,"одежда 2018", null);
        categoryDao.addCategory(category2);

        Category addedCategory2 = categoryDao.getCategory(category2.getId());
        Category addedCategory = categoryDao.getCategory(category.getId());

        List<Category> categories = new ArrayList<>();
        categories.add(addedCategory2);
        categories.add(addedCategory);

        List<Category> allCategories  = categoryDao.getAllCategories();
        Assert.assertEquals(categories, allCategories);
    }




}
