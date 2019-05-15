package net.thumbtack.onlineshop.testService;

import net.thumbtack.onlineshop.dto.requests.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.requests.AdminRegistrationRequest;
import net.thumbtack.onlineshop.dto.requests.ClientRegistrationRequest;
import net.thumbtack.onlineshop.dto.requests.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.responses.AddCategoryResponse;
import net.thumbtack.onlineshop.dto.responses.AdminRegistrationResponse;
import net.thumbtack.onlineshop.dto.responses.ClientRegistrationResponse;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestCategoryService extends TestBaseService {

    @Test
    public void testCategoryAdding() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest request = new AddCategoryRequest("Одежда",null);
        AddCategoryResponse response = categoryService.addCategory(request, token);
        Assert.assertNotEquals(0,response.getId());
        Assert.assertEquals(request.getName(),response.getName());
        Assert.assertNull(response.getParentId());
        Assert.assertNull(response.getParentName());

        String number = response.getId() + "";
        AddCategoryResponse getCategoryResponse = categoryService.getCategory(number, token);
        Assert.assertEquals(response,getCategoryResponse);
    }

    @Test
    public void testSubCategoryAdding() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest request = new AddCategoryRequest("Мужская одежда",null);
        AddCategoryResponse response = categoryService.addCategory(request, token);
        Assert.assertNotEquals(0,response.getId());
        Assert.assertEquals(request.getName(),response.getName());
        Assert.assertNull(response.getParentId());
        Assert.assertNull(response.getParentName());

        String number = response.getId() + "";
        AddCategoryResponse getCategoryResponse = categoryService.getCategory(number, token);
        Assert.assertEquals(response,getCategoryResponse);

        AddCategoryRequest request2 = new AddCategoryRequest("Брюки",response.getId());
        AddCategoryResponse response2 = categoryService.addCategory(request2, token);
        Assert.assertNotEquals(0,response2.getId());
        Assert.assertEquals(request2.getName(),response2.getName());
        Assert.assertEquals((Integer)response.getId(),response2.getParentId());
        Assert.assertEquals(response.getName(),response2.getParentName());

        String number2 = response2.getId() + "";
        AddCategoryResponse getCategoryResponse2 = categoryService.getCategory(number2, token);
        Assert.assertEquals(response2,getCategoryResponse2);
    }

    @Test
    public void testCategoryAddingWithoutPermission() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest clientRegistrationRequest = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        userService.registerClient(clientRegistrationRequest, token);

        AddCategoryRequest request = new AddCategoryRequest("Одежда",null);
        OnlineShopException e = null;
        try {
            categoryService.addCategory(request, token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_NOT_ADMIN, e.getErrorCode());
    }

    @Test
    public void testCategoryGetting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest request = new AddCategoryRequest("Одежда",null);
        AddCategoryResponse response = categoryService.addCategory(request, token);
        Assert.assertNotEquals(0,response.getId());
        Assert.assertEquals(request.getName(),response.getName());
        Assert.assertNull(response.getParentId());
        Assert.assertNull(response.getParentName());

        String number = response.getId() + "";
        AddCategoryResponse getCategoryResponse = categoryService.getCategory(number, token);
        Assert.assertEquals(response,getCategoryResponse);

        String token2 = tokenGenerator.generateToken();
        ClientRegistrationRequest clientRegistrationRequest = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        userService.registerClient(clientRegistrationRequest, token2);

        OnlineShopException e = null;
        try {
            categoryService.getCategory(number, token2);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_NOT_ADMIN, e.getErrorCode());

    }


    @Test
    public void testCategoryEditing() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Мужская одежда",null);
        AddCategoryResponse addCategoryResponse = categoryService.addCategory(addCategoryRequest, token);

        AddCategoryRequest addCategoryRequest2 = new AddCategoryRequest("Брюки",addCategoryResponse.getId());
        AddCategoryResponse addCategoryResponse2 = categoryService.addCategory(addCategoryRequest2, token);

        AddCategoryRequest addCategoryRequest3 = new AddCategoryRequest("Женская одежда",null );
        AddCategoryResponse addCategoryResponse3 = categoryService.addCategory(addCategoryRequest3, token);

        String number2 = addCategoryResponse2.getId()+"";
        EditCategoryRequest request = new EditCategoryRequest(null, addCategoryResponse3.getId());
        AddCategoryResponse response = categoryService.editCategory(request,number2,token);
        Assert.assertEquals(addCategoryRequest2.getName(),response.getName());
        Assert.assertEquals((Integer)addCategoryResponse3.getId(),response.getParentId());
        Assert.assertEquals(addCategoryResponse3.getName(),response.getParentName());

        AddCategoryResponse getCategoryResponse = categoryService.getCategory(number2,token);
        Assert.assertEquals(response, getCategoryResponse);

        EditCategoryRequest request2 = new EditCategoryRequest("Пиджак", null);
        AddCategoryResponse response2 = categoryService.editCategory(request2,number2,token);
        Assert.assertEquals(request2.getName(),response2.getName());
        Assert.assertEquals((Integer)addCategoryResponse3.getId(),response2.getParentId());
        Assert.assertEquals(addCategoryResponse3.getName(),response2.getParentName());

        EditCategoryRequest request3 = new EditCategoryRequest("Брюки", null);

        OnlineShopException e = null;
        try {
            categoryService.editCategory(request3,"one",token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("number of category in address line", e.getField());

    }

    @Test
    public void testCategoryDeleting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest request = new AddCategoryRequest("Одежда",null);
        AddCategoryResponse response = categoryService.addCategory(request, token);
        Assert.assertNotEquals(0,response.getId());
        Assert.assertEquals(request.getName(),response.getName());
        Assert.assertNull(response.getParentId());
        Assert.assertNull(response.getParentName());

        String number = response.getId() + "";
        AddCategoryResponse getCategoryResponse = categoryService.getCategory(number, token);
        Assert.assertEquals(response,getCategoryResponse);

        String deletingResponse = categoryService.deleteCategory(number, token);
        Assert.assertEquals("{}", deletingResponse);

        OnlineShopException e = null;
        try {
            categoryService.getCategory(number, token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.CATEGORY_NOT_EXISTS, e.getErrorCode());

    }


    @Test
    public void testAllCategoryGetting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("Мужская одежда",null);
        AddCategoryResponse addCategoryResponse = categoryService.addCategory(addCategoryRequest, token);

        AddCategoryRequest addCategoryRequest2 = new AddCategoryRequest("Брюки",addCategoryResponse.getId());
        AddCategoryResponse addCategoryResponse2 = categoryService.addCategory(addCategoryRequest2, token);

        AddCategoryRequest addCategoryRequest3 = new AddCategoryRequest("Женская одежда",null );
        AddCategoryResponse addCategoryResponse3 = categoryService.addCategory(addCategoryRequest3, token);

        List<AddCategoryResponse> categoriesWithSubCategories = categoryService.getAllCategories(token);
        Assert.assertEquals(3, categoriesWithSubCategories.size());
        Assert.assertEquals(addCategoryResponse3, categoriesWithSubCategories.get(0));
        Assert.assertEquals(addCategoryResponse, categoriesWithSubCategories.get(1));
        Assert.assertEquals(addCategoryResponse2, categoriesWithSubCategories.get(2));

    }


}
