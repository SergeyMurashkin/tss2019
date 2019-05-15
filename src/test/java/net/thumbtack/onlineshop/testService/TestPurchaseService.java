package net.thumbtack.onlineshop.testService;

import net.thumbtack.onlineshop.dto.requests.*;
import net.thumbtack.onlineshop.dto.responses.*;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestPurchaseService extends TestBaseService {

    @Test
            public void testPurchaseProduct() throws OnlineShopException {

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

        int deposit = 9000;
        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest(deposit + "");
        ClientRegistrationResponse depositResponse = userService.depositMoney(depositMoneyRequest,token2);

        PurchaseProductRequest request = new PurchaseProductRequest(
                0,
                addProductResponse.getName(),
                addProductResponse.getPrice());

        OnlineShopException e = null;
        try{
            purchaseService.purchaseProduct(request,token2);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                "id",
                OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText()),e);

        PurchaseProductRequest request2 = new PurchaseProductRequest(
                addProductResponse.getId(),
                "Apple",
                addProductResponse.getPrice());

        OnlineShopException e2 = null;
        try{
            purchaseService.purchaseProduct(request2,token2);
        } catch (OnlineShopException ex){
            e2=ex;
        }
        Assert.assertNotNull(e2);
        Assert.assertEquals(new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                "name",
                OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + addProductResponse.getName()),e2);

        PurchaseProductRequest request3 = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                1000);

        OnlineShopException e3 = null;
        try{
            purchaseService.purchaseProduct(request3,token2);
        } catch (OnlineShopException ex){
            e3=ex;
        }
        Assert.assertNotNull(e3);
        Assert.assertEquals(new OnlineShopException(
                OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                "price",
                OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + addProductResponse.getPrice()),e3);

        PurchaseProductRequest request4 = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice(),
                1000);

        OnlineShopException e4 = null;
        try{
            purchaseService.purchaseProduct(request4,token2);
        } catch (OnlineShopException ex){
            e4=ex;
        }
        Assert.assertNotNull(e4);
        Assert.assertEquals(new OnlineShopException(
                        OnlineShopErrorCode.PRODUCT_INSUFFICIENT_AMOUNT,
                        "count",
                        OnlineShopErrorCode.PRODUCT_INSUFFICIENT_AMOUNT.getErrorText() + addProductResponse.getCount()),e4);

        PurchaseProductRequest request5 = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice(),
                100);

        OnlineShopException e5 = null;
        try{
            purchaseService.purchaseProduct(request5,token2);
        } catch (OnlineShopException ex){
            e5=ex;
        }
        Assert.assertNotNull(e5);
        Assert.assertEquals(new OnlineShopException(
                OnlineShopErrorCode.DEPOSIT_INSUFFICIENT_AMOUNT,
                null,
                OnlineShopErrorCode.DEPOSIT_INSUFFICIENT_AMOUNT.getErrorText()),e5);

        PurchaseProductRequest request6 = new PurchaseProductRequest(
                addProductResponse.getId(),
                addProductResponse.getName(),
                addProductResponse.getPrice(),
                50);


         PurchaseProductResponse response = purchaseService.purchaseProduct(request6,token2);
         Assert.assertNotEquals(0,response.getId());
         Assert.assertEquals(request6.getId(),response.getId());
         Assert.assertEquals(request6.getName(),response.getName());
         Assert.assertEquals(request6.getPrice(),response.getPrice());
         Assert.assertEquals(request6.getCount(),response.getCount());

         ClientRegistrationResponse getDepositResponse = userService.getBalance(token2);
         Assert.assertEquals(deposit-request6.getPrice()*request6.getCount(), getDepositResponse.getDeposit());

        GetProductResponse getProductResponse = productService.getProduct(addProductResponse.getId()+"",token2);
        Assert.assertEquals(addProductResponse.getCount()-response.getCount(),getProductResponse.getCount());

    }

    @Test
    public void testPurchaseBasketProducts() throws OnlineShopException {

        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

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

        int deposit = 100000;
        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest(deposit + "");
        ClientRegistrationResponse depositResponse = userService.depositMoney(depositMoneyRequest,token2);

        AddProductRequest addProductRequest1 = new AddProductRequest("Яблоко", 100, 90, new ArrayList<>());
        AddProductResponse addProductResponse1 = productService.addProduct(addProductRequest1, token);

        AddProductRequest addProductRequest2 = new AddProductRequest("Груша", 150, 100, new ArrayList<>());
        AddProductResponse addProductResponse2 = productService.addProduct(addProductRequest2, token);

        AddProductRequest addProductRequest3 = new AddProductRequest("Апельсин", 200, 110, new ArrayList<>());
        AddProductResponse addProductResponse3 = productService.addProduct(addProductRequest3, token);

        PurchaseProductRequest addProductInBasketRequest = new PurchaseProductRequest(
                addProductResponse1.getId(),
                addProductResponse1.getName(),
                addProductResponse1.getPrice(),
                50);
        List<GetProductResponse> addProductInBasketResponse =
                basketService.addProductInBasket(addProductInBasketRequest, token2);

        PurchaseProductRequest addProductInBasketRequest2 = new PurchaseProductRequest(
                addProductResponse2.getId(),
                addProductResponse2.getName(),
                addProductResponse2.getPrice(),
                50);
        List<GetProductResponse> addProductInBasketResponse2 =
                basketService.addProductInBasket(addProductInBasketRequest2, token2);

        PurchaseProductRequest addProductInBasketRequest3 = new PurchaseProductRequest(
                addProductResponse3.getId(),
                addProductResponse3.getName(),
                addProductResponse3.getPrice(),
                50);
        List<GetProductResponse> addProductInBasketResponse3 =
                basketService.addProductInBasket(addProductInBasketRequest3, token2);

        PurchaseProductFromBasketRequest apple = new PurchaseProductFromBasketRequest(
                addProductResponse1.getId(),
                addProductResponse1.getName(),
                addProductResponse1.getPrice(),
                null);

        PurchaseProductFromBasketRequest pear = new PurchaseProductFromBasketRequest(
                addProductResponse2.getId(),
                addProductResponse2.getName(),
                addProductResponse2.getPrice(),
                1);

        PurchaseProductFromBasketRequest orange = new PurchaseProductFromBasketRequest(
                addProductResponse3.getId(),
                addProductResponse3.getName(),
                addProductResponse3.getPrice(),
                100);

        PurchaseProductFromBasketResponse response =
                purchaseService.purchaseProductsFromBasket(Arrays.asList(apple,pear,orange),token2);
        Assert.assertEquals(3, response.getBought().size());
        Assert.assertTrue(response.getRemaining().isEmpty());

        PurchaseProductResponse boughtApple = new PurchaseProductResponse(
                addProductResponse1.getId(),
                addProductResponse1.getName(),
                addProductResponse1.getPrice(),
                50);

        PurchaseProductResponse boughtPear = new PurchaseProductResponse(
                addProductResponse2.getId(),
                addProductResponse2.getName(),
                addProductResponse2.getPrice(),
                1);

        PurchaseProductResponse boughtOrange = new PurchaseProductResponse(
                addProductResponse3.getId(),
                addProductResponse3.getName(),
                addProductResponse3.getPrice(),
                50);

        Assert.assertEquals(Arrays.asList(boughtApple,boughtPear,boughtOrange), response.getBought());

    }


    @Test
    public void testFailPurchaseBasketProducts() throws OnlineShopException {

        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest adminRegistrationRequest = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(adminRegistrationRequest, token);

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

        int deposit = 100000;
        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest(deposit + "");
        ClientRegistrationResponse depositResponse = userService.depositMoney(depositMoneyRequest,token2);

        AddProductRequest addProductRequest1 = new AddProductRequest("Яблоко", 100, 90, new ArrayList<>());
        AddProductResponse addProductResponse1 = productService.addProduct(addProductRequest1, token);

        AddProductRequest addProductRequest2 = new AddProductRequest("Груша", 150, 100, new ArrayList<>());
        AddProductResponse addProductResponse2 = productService.addProduct(addProductRequest2, token);

        AddProductRequest addProductRequest3 = new AddProductRequest("Апельсин", 200, 110, new ArrayList<>());
        AddProductResponse addProductResponse3 = productService.addProduct(addProductRequest3, token);

        PurchaseProductRequest addProductInBasketRequest = new PurchaseProductRequest(
                addProductResponse1.getId(),
                addProductResponse1.getName(),
                addProductResponse1.getPrice(),
                50);
        List<GetProductResponse> addProductInBasketResponse =
                basketService.addProductInBasket(addProductInBasketRequest, token2);

        PurchaseProductRequest addProductInBasketRequest2 = new PurchaseProductRequest(
                addProductResponse2.getId(),
                addProductResponse2.getName(),
                addProductResponse2.getPrice(),
                50);
        List<GetProductResponse> addProductInBasketResponse2 =
                basketService.addProductInBasket(addProductInBasketRequest2, token2);

        PurchaseProductRequest addProductInBasketRequest3 = new PurchaseProductRequest(
                addProductResponse3.getId(),
                addProductResponse3.getName(),
                addProductResponse3.getPrice(),
                50);
        List<GetProductResponse> addProductInBasketResponse3 =
                basketService.addProductInBasket(addProductInBasketRequest3, token2);

        PurchaseProductFromBasketRequest nonexistentProduct = new PurchaseProductFromBasketRequest(
                0,
                addProductResponse1.getName(),
                addProductResponse1.getPrice(),
                50);


        productService.deleteProduct(addProductResponse2.getId()+"",token);
        PurchaseProductFromBasketRequest pear = new PurchaseProductFromBasketRequest(
                addProductResponse2.getId(),
                addProductResponse2.getName(),
                addProductResponse2.getPrice(),
                50);

        PurchaseProductFromBasketRequest orange = new PurchaseProductFromBasketRequest(
                addProductResponse3.getId(),
                addProductResponse3.getName(),
                addProductResponse3.getPrice(),
                100);



        PurchaseProductFromBasketResponse response =
                purchaseService.purchaseProductsFromBasket(Arrays.asList(nonexistentProduct,pear,orange),token2);
        Assert.assertEquals(1, response.getBought().size());
        Assert.assertEquals(2, response.getRemaining().size());



        PurchaseProductResponse boughtOrange = new PurchaseProductResponse(
                addProductResponse3.getId(),
                addProductResponse3.getName(),
                addProductResponse3.getPrice(),
                50);
        Assert.assertEquals(Arrays.asList(boughtOrange), response.getBought());

        PurchaseProductResponse remainingApple = new PurchaseProductResponse(
                addProductResponse1.getId(),
                addProductResponse1.getName(),
                addProductResponse1.getPrice(),
                50);
        PurchaseProductResponse remainingPear = new PurchaseProductResponse(
                addProductResponse2.getId(),
                addProductResponse2.getName(),
                addProductResponse2.getPrice(),
                50);
        Assert.assertEquals(Arrays.asList(remainingPear,remainingApple), response.getRemaining());


    }



}
