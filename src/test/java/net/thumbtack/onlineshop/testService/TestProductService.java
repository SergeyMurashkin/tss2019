package net.thumbtack.onlineshop.testService;

import net.thumbtack.onlineshop.dto.requests.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.requests.AddProductRequest;
import net.thumbtack.onlineshop.dto.requests.AdminRegistrationRequest;
import net.thumbtack.onlineshop.dto.requests.EditProductRequest;
import net.thumbtack.onlineshop.dto.responses.AddCategoryResponse;
import net.thumbtack.onlineshop.dto.responses.AddProductResponse;
import net.thumbtack.onlineshop.dto.responses.GetProductResponse;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestProductService extends TestBaseService{

    @Test
    public void testProductAdding() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Фрукты",null);
        AddCategoryResponse addCategoryResponse = categoryService.addCategory(addCategoryRequest, token);
        AddCategoryRequest addCategoryRequest2 = new AddCategoryRequest("Овощи",null);
        AddCategoryResponse addCategoryResponse2 = categoryService.addCategory(addCategoryRequest2, token);

        AddProductRequest request = new AddProductRequest(
                "Яблоко",
                100,
                100,
                Arrays.asList(addCategoryResponse.getId(),addCategoryResponse2.getId()));
        AddProductResponse response = productService.addProduct(request, token);
        Assert.assertNotEquals(0, response.getId());
        Assert.assertEquals(request.getName(), response.getName());
        Assert.assertEquals(request.getPrice(), response.getPrice());
        Assert.assertEquals(request.getCount(), response.getCount());
        List<Integer> sortedByNameCategoriesId = Arrays.asList(addCategoryResponse2.getId(),addCategoryResponse.getId());
        Assert.assertEquals(sortedByNameCategoriesId, response.getCategories());

        AddProductRequest request2 = new AddProductRequest(
                "Яблоко",
                100,
                100,
                Arrays.asList(addCategoryResponse.getId(),addCategoryResponse2.getId()));
        AddProductResponse response2 = productService.addProduct(request2, token);
        Assert.assertNotEquals(0, response2.getId());
        Assert.assertNotEquals(response.getId(), response2.getId());
        Assert.assertEquals(request2.getName(), response2.getName());
        Assert.assertEquals(request2.getPrice(), response2.getPrice());
        Assert.assertEquals(request2.getCount(), response2.getCount());
        List<Integer> sortedByNameCategoriesId2 = Arrays.asList(addCategoryResponse2.getId(),addCategoryResponse.getId());
        Assert.assertEquals(sortedByNameCategoriesId2, response2.getCategories());

        AddProductRequest request3 = new AddProductRequest(
                "Соль",
                50,
                100,
                new ArrayList<>());
        AddProductResponse response3 = productService.addProduct(request3, token);
        Assert.assertNotEquals(0, response3.getId());
        Assert.assertEquals(request3.getName(), response3.getName());
        Assert.assertEquals(request3.getPrice(), response3.getPrice());
        Assert.assertEquals(request3.getCount(), response3.getCount());
        Assert.assertEquals(new ArrayList<>(), response3.getCategories());
    }

    @Test
    public void testProductEditing() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Фрукты",null);
        AddCategoryResponse addCategoryResponse = categoryService.addCategory(addCategoryRequest, token);
        AddCategoryRequest addCategoryRequest2 = new AddCategoryRequest("Овощи",null);
        AddCategoryResponse addCategoryResponse2 = categoryService.addCategory(addCategoryRequest2, token);

        AddProductRequest request = new AddProductRequest(
                "Яблоко",
                100,
                100,
                Arrays.asList(addCategoryResponse2.getId()));
        AddProductResponse response = productService.addProduct(request, token);

        String number = response.getId() + "";

        EditProductRequest request2 = new EditProductRequest(
                "Груши",
                110,
                90,
                Arrays.asList(addCategoryResponse.getId()));
        AddProductResponse response2 = productService.editProduct(request2, number, token);
        Assert.assertEquals(response.getId(), response2.getId());
        Assert.assertEquals(request2.getName(), response2.getName());
        Assert.assertEquals(request2.getPrice(), (Integer) response2.getPrice());
        Assert.assertEquals(request2.getCount(), (Integer) response2.getCount());
        Assert.assertEquals(Arrays.asList(addCategoryResponse.getId()), response2.getCategories());
    }

    @Test
    public void testProductDeleting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Фрукты",null);
        AddCategoryResponse addCategoryResponse = categoryService.addCategory(addCategoryRequest, token);

        AddProductRequest request = new AddProductRequest(
                "Яблоко",
                100,
                100,
                Arrays.asList(addCategoryResponse.getId()));
        AddProductResponse response = productService.addProduct(request, token);

        String number = response.getId() + "";

        productService.getProduct(number,token);
        productService.deleteProduct(number,token);
        OnlineShopException e = null;
        try{
            productService.getProduct(number,token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.PRODUCT_NOT_EXISTS, e.getErrorCode());

    }


    @Test
    public void testProductGetting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Фрукты",null);
        AddCategoryResponse addCategoryResponse = categoryService.addCategory(addCategoryRequest, token);

        AddProductRequest request = new AddProductRequest(
                "Яблоко",
                100,
                100,
                Arrays.asList(addCategoryResponse.getId()));
        AddProductResponse response = productService.addProduct(request, token);

        String number = response.getId() + "";

        GetProductResponse getProductResponse = productService.getProduct(number,token);
        Assert.assertEquals(response.getId(), getProductResponse.getId());
        Assert.assertEquals(response.getName(), getProductResponse.getName());
        Assert.assertEquals(response.getPrice(), getProductResponse.getPrice());
        Assert.assertEquals(response.getCount(), getProductResponse.getCount());
        Assert.assertEquals(addCategoryResponse.getName(), getProductResponse.getCategories().get(0));


        OnlineShopException e = null;
        try{
            productService.getProduct("one",token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                "number of product in address line",
                "Use numbers after {api/products/} "), e);

        OnlineShopException e2 = null;
        try{
            productService.getProduct("0",token);
        } catch (OnlineShopException ex){
            e2=ex;
        }
        Assert.assertNotNull(e2);
        Assert.assertEquals(new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                "number of product in address line",
                OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText()), e2);
    }

    @Test
    public void testProductsListGetting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest addCategoryRequest1 = new AddCategoryRequest("Фрукт",null);
        AddCategoryResponse fruit = categoryService.addCategory(addCategoryRequest1, token);

        AddCategoryRequest addCategoryRequest2 = new AddCategoryRequest("Овощь",null);
        AddCategoryResponse vegetable = categoryService.addCategory(addCategoryRequest2, token);

        AddCategoryRequest addCategoryRequest3 = new AddCategoryRequest("Ягода",null);
        AddCategoryResponse berry = categoryService.addCategory(addCategoryRequest3, token);

        AddProductRequest addProductRequest1 = new AddProductRequest("Груша", 200, 20,
                Arrays.asList(fruit.getId()));
        AddProductResponse addProductResponse1 = productService.addProduct(addProductRequest1, token);

        AddProductRequest addProductRequest2 = new AddProductRequest("Яблоко", 190, 25,
                Arrays.asList(fruit.getId()));
        AddProductResponse addProductResponse2 = productService.addProduct(addProductRequest2, token);

        AddProductRequest addProductRequest3 = new AddProductRequest("Морковь", 180, 30,
                Arrays.asList(vegetable.getId()));
        AddProductResponse addProductResponse3 = productService.addProduct(addProductRequest3, token);

        AddProductRequest addProductRequest4 = new AddProductRequest("Помидор", 170, 35,
                Arrays.asList(vegetable.getId(),berry.getId()));
        AddProductResponse addProductResponse4 = productService.addProduct(addProductRequest4, token);

        AddProductRequest addProductRequest5 = new AddProductRequest("Виноград", 160, 40,
                Arrays.asList(berry.getId()));
        AddProductResponse addProductResponse5 = productService.addProduct(addProductRequest5, token);

        AddProductRequest addProductRequest6 = new AddProductRequest("Клюква", 150, 45,
                Arrays.asList(berry.getId()));
        AddProductResponse addProductResponse6 = productService.addProduct(addProductRequest6, token);

        AddProductRequest addProductRequest7 = new AddProductRequest("Соль", 140, 50,
                new ArrayList<>());
        AddProductResponse addProductResponse7 = productService.addProduct(addProductRequest7, token);

        AddProductRequest addProductRequest8 = new AddProductRequest("Перец", 130, 55,
                new ArrayList<>());
        AddProductResponse addProductResponse8 = productService.addProduct(addProductRequest8, token);

        GetProductResponse pear = productService.getProduct(addProductResponse1.getId()+"",token);
        GetProductResponse apple = productService.getProduct(addProductResponse2.getId()+"",token);
        GetProductResponse carrot = productService.getProduct(addProductResponse3.getId()+"",token);
        GetProductResponse tomato = productService.getProduct(addProductResponse4.getId()+"",token);
        GetProductResponse grapes = productService.getProduct(addProductResponse5.getId()+"",token);
        GetProductResponse cranberry = productService.getProduct(addProductResponse6.getId()+"",token);
        GetProductResponse salt = productService.getProduct(addProductResponse7.getId()+"",token);
        GetProductResponse pepper = productService.getProduct(addProductResponse8.getId()+"",token);

        GetProductResponse tomatoBerry = productService.getProduct(addProductResponse4.getId()+"",token);

        List<GetProductResponse> allProductsByProductOrderSample =
                Arrays.asList(grapes, pear, cranberry, carrot, pepper, tomato, salt, apple);
        List<GetProductResponse> allProductsByProductOrder =
                productService.getProductsByCategory(null,"product",token);
        Assert.assertEquals(allProductsByProductOrderSample,allProductsByProductOrder);

        List<GetProductResponse> productsByProductOrderSample = Arrays.asList(grapes, pear, cranberry, tomato, apple);
        List<GetProductResponse> productsByProductOrder =
                productService.getProductsByCategory(Arrays.asList(berry.getId(),fruit.getId()),"product",token);
        Assert.assertEquals(productsByProductOrderSample,productsByProductOrder);

        List<GetProductResponse> productsWithoutCategoriesSample = Arrays.asList(pepper,salt);
        List<GetProductResponse> productsWithoutCategories =
                productService.getProductsByCategory(new ArrayList<>(),"product",token);
        Assert.assertEquals(productsWithoutCategoriesSample,productsWithoutCategories);

        tomato.getCategories().remove("Ягода");
        tomatoBerry.getCategories().remove("Овощь");

        List<GetProductResponse> allProductsByCategoryOrderSample =
                Arrays.asList(pepper, salt, carrot, tomato, pear, apple, grapes,  cranberry, tomatoBerry);
        List<GetProductResponse> allProductsByCategoryOrder =
                productService.getProductsByCategory(null,"category",token);
        Assert.assertEquals(allProductsByCategoryOrderSample,allProductsByCategoryOrder);

        List<GetProductResponse> productsByCategoryOrderSample =
                Arrays.asList(pear, apple, grapes, cranberry, tomatoBerry);
        List<GetProductResponse> productsByCategoryOrder =
                productService.getProductsByCategory(Arrays.asList(berry.getId(),fruit.getId()),"category",token);
        Assert.assertEquals(productsByCategoryOrderSample,productsByCategoryOrder);

    }



}
