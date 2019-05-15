package net.thumbtack.onlineshop.testDao;

import net.thumbtack.onlineshop.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestProductDao extends TestBaseDao {

    @Test
    public void testProductAdding() throws OnlineShopException {
        Product product = new Product(0, "Брюки", 1000, 20, null, null);
        productDao.addProduct(product,null);
        Assert.assertNotEquals(0, product.getId());
        Category category = new Category(0,"одежда 2019", null);
        categoryDao.addCategory(category);
        Product product2 = new Product(0, "Брюки", 500, 10, null, null);
        List<Integer> categoriesId = new ArrayList<>();
        categoriesId.add(category.getId());
        productDao.addProduct(product2, categoriesId);
        Assert.assertNotEquals(0, product2.getId());
        Assert.assertNotEquals(product.getId(), product2.getId());
    }

    @Test
    public void testProductAddingWithNonexistentCategories() {
        List<Integer> categoriesId = Arrays.asList(1,2,3);
        Product product = new Product(0, "Брюки", 1000, 20, null, null);
        OnlineShopException e = null;
        try {
            productDao.addProduct(product, categoriesId);
        } catch(OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("categoriesId", e.getField());
    }


    @Test
    public void testNonexistentProductGetting() {
        OnlineShopException e = null;
        try {
            productDao.getProduct(0);
        } catch(OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("number of product in address line", e.getField());
    }

    @Test
    public void testProductGetting() throws OnlineShopException {

        Product product = new Product(0, "Брюки", 1000, 20, null, null);
        productDao.addProduct(product,null);
        Product addedProduct = productDao.getProduct(product.getId());
        Assert.assertNotEquals(0, addedProduct.getId());
        Assert.assertEquals(product.getId(),addedProduct.getId());
        Assert.assertEquals(product.getName(),addedProduct.getName());
        Assert.assertEquals(product.getPrice(),addedProduct.getPrice());
        Assert.assertEquals(product.getCount(),addedProduct.getCount());

        Category category = new Category(0,"одежда 2019", null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());

        Product product2 = new Product(0, "Брюки", 500, 10, null, null);
        List<Integer> categoriesId = new ArrayList<>();
        categoriesId.add(addedCategory.getId());
        productDao.addProduct(product2, categoriesId);
        Product addedProduct2 = productDao.getProduct(product2.getId());
        Assert.assertNotEquals(0, addedProduct2.getId());
        Assert.assertEquals(product2.getId(),addedProduct2.getId());
        Assert.assertEquals(product2.getName(),addedProduct2.getName());
        Assert.assertEquals(product2.getPrice(),addedProduct2.getPrice());
        Assert.assertEquals(product2.getCount(),addedProduct2.getCount());
        Assert.assertEquals(addedCategory.getId(), addedProduct2.getCategories().get(0).getId());
        Assert.assertEquals(addedCategory.getName(), addedProduct2.getCategories().get(0).getName());
    }

    @Test
    public void testProductEditing() throws OnlineShopException {

        Category category = new Category(0,"Фрукты", null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        List<Integer> categoriesId = new ArrayList<>();
        categoriesId.add(addedCategory.getId());

        Product product = new Product(0, "Новые груши", 200, 20, null, null);
        productDao.addProduct(product,categoriesId);
        Product addedProduct = productDao.getProduct(product.getId());

        Product newProduct = new Product(addedProduct.getId(),"Груши",
                50,10,null, addedProduct.getVersion());

        productDao.editProduct(newProduct, null);
        Product editedProduct = productDao.getProduct(newProduct.getId());
        Assert.assertEquals(addedProduct.getId(),editedProduct.getId());
        Assert.assertEquals("Груши",editedProduct.getName());
        Assert.assertEquals(50,editedProduct.getPrice());
        Assert.assertEquals(10,editedProduct.getCount());
        Assert.assertEquals((Integer)1,editedProduct.getVersion());
        Assert.assertEquals(1,editedProduct.getCategories().size());
        Assert.assertEquals("Фрукты",editedProduct.getCategories().get(0).getName());

        Product newProduct2 = new Product(editedProduct.getId(),"Старые груши",
                25, 5,null, editedProduct.getVersion());

        System.out.println(newProduct2);

        Category category2 = new Category(0,"Старые фрукты", category.getId());
        categoryDao.addCategory(category2);
        Category addedCategory2 = categoryDao.getCategory(category2.getId());
        List<Integer> categoriesId2 = new ArrayList<>();
        categoriesId2.add(addedCategory2.getId());

        productDao.editProduct(newProduct2, categoriesId2);
        Product editedProduct2 = productDao.getProduct(newProduct2.getId());
        Assert.assertEquals(addedProduct.getId(),editedProduct2.getId());
        Assert.assertEquals("Старые груши",editedProduct2.getName());
        Assert.assertEquals(25,editedProduct2.getPrice());
        Assert.assertEquals(5,editedProduct2.getCount());
        Assert.assertEquals((Integer)2,editedProduct2.getVersion());
        Assert.assertEquals(1,editedProduct2.getCategories().size());
        Assert.assertEquals("Старые фрукты",editedProduct2.getCategories().get(0).getName());

        Product newProduct3 = new Product(editedProduct.getId(), "Гнилые груши",
                1, 1, null, editedProduct2.getVersion());

        productDao.editProduct(newProduct3, new ArrayList<>());
        Product editedProduct3 = productDao.getProduct(newProduct3.getId());
        Assert.assertEquals(addedProduct.getId(),editedProduct3.getId());
        Assert.assertEquals("Гнилые груши",editedProduct3.getName());
        Assert.assertEquals(1,editedProduct3.getPrice());
        Assert.assertEquals(1,editedProduct3.getCount());
        Assert.assertEquals((Integer)3,editedProduct3.getVersion());
        Assert.assertEquals(0,editedProduct3.getCategories().size());
    }

    @Test
    public void testProductEditingWithNonexistentCategories() throws OnlineShopException {
        Category category = new Category(0,"Фрукты", null);
        categoryDao.addCategory(category);
        Category addedCategory = categoryDao.getCategory(category.getId());
        List<Integer> categoriesId = new ArrayList<>();
        categoriesId.add(addedCategory.getId());

        Product product = new Product(0, "Новые груши",
                200, 20, null, null);
        productDao.addProduct(product,categoriesId);
        Product addedProduct = productDao.getProduct(product.getId());
        Product newProduct = new Product(addedProduct.getId(), "Груши",
                50, 10, null, addedProduct.getVersion());
        List<Integer> categoriesId2 = new ArrayList<>();
        categoriesId2.add(0);
        OnlineShopException e=null;
        try {
            productDao.editProduct(newProduct, categoriesId2);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("categoriesId", e.getField());
    }

    @Test
    public void testProductEditingWithWrongVersion() throws OnlineShopException {
        Product product = new Product(0, "Новые груши",
                200, 20, null, null);
        productDao.addProduct(product,null);
        Product addedProduct = productDao.getProduct(product.getId());
        Product newProduct = new Product(addedProduct.getId(),"Груши",
                50,10,null,1);
        OnlineShopException e=null;
        try {
            productDao.editProduct(newProduct, null);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("The state of the product was changed during preparation. Try again.", e.getMessage());
    }

    @Test
    public void testAllProductsByProductOrderGetting() throws OnlineShopException {

        Category fruit = new Category(0,"Фрукт", null);
        categoryDao.addCategory(fruit);
        fruit = categoryDao.getCategory(fruit.getId());
        fruit = new Category(fruit.getId(),fruit.getName(),null);

        Category vegetable = new Category(0,"Овощь", null);
        categoryDao.addCategory(vegetable);
        vegetable = categoryDao.getCategory(vegetable.getId());
        vegetable = new Category(vegetable.getId(),vegetable.getName(),null);

        Category berry = new Category(0,"Ягода", null);
        categoryDao.addCategory(berry);
        berry = categoryDao.getCategory(berry.getId());
        berry = new Category(berry.getId(),berry.getName(),null);

        Product pear = new Product(0,"Груша",200,20,null,null);
        productDao.addProduct(pear, Arrays.asList(fruit.getId()));
        pear = productDao.getProduct(pear.getId());

        Product apple = new Product(0,"Яблоко",190,25,null,null);
        productDao.addProduct(apple, Arrays.asList(fruit.getId()));
        apple = productDao.getProduct(apple.getId());

        Product carrot = new Product(0,"Морковь",180,30,null,null);
        productDao.addProduct(carrot, Arrays.asList(vegetable.getId()));
        carrot = productDao.getProduct(carrot.getId());

        Product tomato = new Product(0,"Помидор",170,35,null,null);
        productDao.addProduct(tomato, Arrays.asList(vegetable.getId(), berry.getId()));
        tomato = productDao.getProduct(tomato.getId());

        Product grapes = new Product(0,"Виноград",160,40,null,null);
        productDao.addProduct(grapes, Arrays.asList(berry.getId()));
        grapes = productDao.getProduct(grapes.getId());

        Product cranberry = new Product(0,"Клюква",150,45,null,null);
        productDao.addProduct(cranberry, Arrays.asList(berry.getId()));
        cranberry = productDao.getProduct(cranberry.getId());

        Product salt = new Product(0,"Соль",140,50,null,null);
        productDao.addProduct(salt,null);
        salt = productDao.getProduct(salt.getId());

        Product pepper = new Product(0,"Перец",130,55,null,null);
        productDao.addProduct(pepper,null);
        pepper = productDao.getProduct(pepper.getId());

        List<Product> allProductsByProductOrder = productDao.getAllProductsByProductOrder();
        List<Product> allProductsByProductOrderSample =
                Arrays.asList(grapes, pear, cranberry, carrot, pepper, tomato, salt, apple);
        Assert.assertEquals(8, allProductsByProductOrder.size());
        Assert.assertEquals(allProductsByProductOrderSample,allProductsByProductOrder);

        List<Product> productsByProductOrder = productDao.getProductsByProductOrder(Arrays.asList(berry.getId(),fruit.getId()));
        List<Product> productsByProductOrderSample =
                Arrays.asList(grapes, pear, cranberry, tomato, apple);
        Assert.assertEquals(5, productsByProductOrder.size());
        Assert.assertEquals(productsByProductOrderSample,productsByProductOrder);

        List<Product> productsWithoutCategories = productDao.getProductsWithoutCategories();
        List<Product> productsWithoutCategoriesSample =
                Arrays.asList(pepper, salt);
        Assert.assertEquals(2, productsWithoutCategories.size());
        Assert.assertEquals(productsWithoutCategoriesSample, productsWithoutCategories);

        List<Product> allProductsByCategoryOrder = productDao.getAllProductsByCategoryOrder();
        Product tomatoBerry = new Product(tomato.getId(),tomato.getName(),
                tomato.getPrice(),tomato.getCount(),Arrays.asList(berry),tomato.getVersion());
        List<Product> allProductsByCategoryOrderSample =
                Arrays.asList(pepper, salt, carrot, tomato, pear, apple, grapes,  cranberry, tomatoBerry);
        allProductsByCategoryOrderSample.get(3).setCategories(Arrays.asList(vegetable));
        Assert.assertEquals(9,allProductsByCategoryOrder.size());
        Assert.assertEquals(allProductsByCategoryOrderSample,allProductsByCategoryOrder);

        List<Product> productsByCategoryOrder = productDao.getProductsByCategoryOrder(Arrays.asList(berry.getId(),fruit.getId()));
        List<Product> productsByCategoryOrderSample =
                Arrays.asList( pear, apple, grapes, cranberry, tomatoBerry);
        Assert.assertEquals(5, productsByProductOrder.size());
        Assert.assertEquals(productsByCategoryOrderSample,productsByCategoryOrder);

    }

    @Test
    public void testProductDeleting() throws OnlineShopException {

        Product product = new Product(0, "Брюки", 1000, 20, null, null);
        productDao.addProduct(product, null);
        productDao.getProduct(product.getId());
        productDao.deleteProduct(product.getId());

        OnlineShopException e = null;
        try {
            productDao.getProduct(product.getId());
        } catch(OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("number of product in address line", e.getField());
    }


}
