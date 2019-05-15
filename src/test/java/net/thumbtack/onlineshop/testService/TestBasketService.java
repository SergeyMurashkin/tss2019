package net.thumbtack.onlineshop.testService;

import net.thumbtack.onlineshop.dto.requests.AddProductRequest;
import net.thumbtack.onlineshop.dto.requests.AdminRegistrationRequest;
import net.thumbtack.onlineshop.dto.requests.ClientRegistrationRequest;
import net.thumbtack.onlineshop.dto.requests.PurchaseProductRequest;
import net.thumbtack.onlineshop.dto.responses.AddProductResponse;
import net.thumbtack.onlineshop.dto.responses.GetProductResponse;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.Product;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestBasketService extends TestBaseService {

    @Test
    public void testBasketProductAdding() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddProductRequest addProductRequest = new AddProductRequest(
                "Яблоко",
                100,
                100,
                new ArrayList<>());
        AddProductResponse addProductResponse = productService.addProduct(addProductRequest, token);

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

        PurchaseProductRequest request = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice());

        List<GetProductResponse> responses = basketService.addProductInBasket(request, token2);
        Assert.assertEquals(1, responses.size());
        Assert.assertEquals(1, responses.get(0).getCount());
        Assert.assertEquals(addProductResponse.getId(), responses.get(0).getId());
        Assert.assertEquals("Яблоко", responses.get(0).getName());
        Assert.assertEquals(100, responses.get(0).getPrice());


        PurchaseProductRequest request2 = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice(),
                1000);

        List<GetProductResponse> responses2 = basketService.addProductInBasket(request2, token2);
        Assert.assertEquals(1, responses2.size());
        Assert.assertEquals(1000, responses2.get(0).getCount());
        Assert.assertEquals(addProductResponse.getId(), responses2.get(0).getId());
        Assert.assertEquals("Яблоко", responses2.get(0).getName());
        Assert.assertEquals(100, responses2.get(0).getPrice());

    }

    @Test
    public void testBasketProductFailAdding() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddProductRequest addProductRequest = new AddProductRequest(
                "Яблоко",
                100,
                100,
                new ArrayList<>());
        AddProductResponse addProductResponse = productService.addProduct(addProductRequest, token);

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

        PurchaseProductRequest request = new PurchaseProductRequest(
                0,
                addProductResponse.getName(),
                addProductResponse.getPrice());

        OnlineShopException e = null;
        try {
            basketService.addProductInBasket(request, token2);
        } catch(OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals( new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                "id",
                OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText()), e);


        PurchaseProductRequest request2 = new PurchaseProductRequest(
                addProductResponse.getId(),
                "Apple",
                addProductResponse.getPrice());

        OnlineShopException e2 = null;
        try {
            basketService.addProductInBasket(request2, token2);
        } catch(OnlineShopException ex){
            e2=ex;
        }
        Assert.assertNotNull(e2);
        Assert.assertEquals( new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                "name",
                OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + addProductResponse.getName()),e2);


        PurchaseProductRequest request3 = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                1000);

        OnlineShopException e3 = null;
        try {
            basketService.addProductInBasket(request3, token2);
        } catch(OnlineShopException ex){
            e3=ex;
        }
        Assert.assertNotNull(e3);
        Assert.assertEquals( new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                "price",
                OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + addProductResponse.getPrice()), e3);

    }


    @Test
    public void testBasketProductDeleting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddProductRequest addProductRequest = new AddProductRequest(
                "Яблоко",
                100,
                100,
                new ArrayList<>());
        AddProductResponse addProductResponse = productService.addProduct(addProductRequest, token);

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

        PurchaseProductRequest request = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice());

        basketService.addProductInBasket(request, token2);

        OnlineShopException e = null;
        try {
            basketService.deleteProductFromBasket("one",token2);
        } catch(OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals( new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                "number of product in address line",
                "Use numbers after {api/baskets/}"), e);


        String number = addProductResponse.getId() + "";
        String response = basketService.deleteProductFromBasket(number,token2);
        Assert.assertEquals("{}",response);

    }


    @Test
    public void testClientBasketGetting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddProductRequest addProductRequest = new AddProductRequest(
                "Яблоко",
                100,
                100,
                new ArrayList<>());
        AddProductResponse addProductResponse = productService.addProduct(addProductRequest, token);

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

        PurchaseProductRequest request = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice(),
                1000);

        List<GetProductResponse> responses = basketService.addProductInBasket(request, token2);
        List<GetProductResponse> clientBasket = basketService.getClientBasket(token2);
        Assert.assertEquals(responses,clientBasket);


    }

    @Test
    public void testBasketProductQuantityChanging() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

        AddProductRequest addProductRequest = new AddProductRequest(
                "Яблоко",
                100,
                100,
                new ArrayList<>());
        AddProductResponse addProductResponse = productService.addProduct(addProductRequest, token);

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

        PurchaseProductRequest request = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice());

        List<GetProductResponse> responses = basketService.addProductInBasket(request, token2);

        PurchaseProductRequest request2 = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice(),
                1000);

        List<GetProductResponse> responses2 = basketService.changeBasketProductQuantity(request2,token2);
        responses.get(0).setCount(1000);
        Assert.assertEquals(responses,responses2);


    }






}
