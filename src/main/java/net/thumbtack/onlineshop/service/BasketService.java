package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.BasketDao;
import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.daoImpl.BasketDaoImpl;
import net.thumbtack.onlineshop.daoImpl.ProductDaoImpl;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.dto.requests.PurchaseProductRequest;
import net.thumbtack.onlineshop.dto.responses.GetProductResponse;
import net.thumbtack.onlineshop.model.*;

import java.util.ArrayList;
import java.util.List;

public class BasketService {

    private BasketDao basketDao = new BasketDaoImpl();
    private ProductDao productDao = new ProductDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    public List<GetProductResponse> addProductInBasket(PurchaseProductRequest request,
                                                       String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        Client client = userDao.getClient(user);
        Product productToBasket = createProduct(request);
        Product product;
        try {
            product = productDao.getProduct(productToBasket.getId());
        } catch (OnlineShopException ex){
            ex.setField("id");
            throw ex;
        }
        checkProductNameActuality(product, productToBasket);
        checkProductPriceActuality(product, productToBasket);
        basketDao.addProductInBasket(client, productToBasket);
        List<Product> basketProducts = basketDao.getClientBasket(client);
        List<GetProductResponse> responses = new ArrayList<>();
        for (Product productFromBasket : basketProducts) {
            responses.add(createGetProductResponse(productFromBasket));
        }
        return responses;
    }

    public String deleteProductFromBasket(String number, String cookieValue) throws OnlineShopException {
        Integer productId = getBasketProductIdFromAddressLine(number);
        User user = userDao.getActualUser(cookieValue);
        Client client = userDao.getClient(user);
        basketDao.deleteProductFromBasket(client, productId);
        return "{}";
    }


    public List<GetProductResponse> getClientBasket(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        Client client = userDao.getClient(user);
        List<Product> basketProducts = basketDao.getClientBasket(client);
        List<GetProductResponse> responses = new ArrayList<>();
        for (Product product : basketProducts) {
            responses.add(createGetProductResponse(product));
        }
        return responses;
    }

    public List<GetProductResponse> changeBasketProductQuantity(PurchaseProductRequest request, String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        Client client = userDao.getClient(user);
        Product newBasketProduct = createProduct(request);
        Product basketProduct = basketDao.getBasketProduct(client, newBasketProduct.getId());
        checkProductNameActuality(basketProduct, newBasketProduct);
        checkProductPriceActuality(basketProduct, newBasketProduct);
        basketDao.changeBasketProductQuantity(client, newBasketProduct);
        List<Product> basketProducts = basketDao.getClientBasket(client);
        List<GetProductResponse> responses = new ArrayList<>();
        for (Product productFromBasket : basketProducts) {
            responses.add(createGetProductResponse(productFromBasket));
        }
        return responses;
    }

    private Product createProduct(PurchaseProductRequest request) {
        return new Product(
                request.getId(),
                request.getName(),
                request.getPrice(),
                request.getCount(),
                new ArrayList<>(),
                null);
    }

    private void checkProductNameActuality(Product product, Product productToBasket) throws OnlineShopException {
        if (!product.getName().equals(productToBasket.getName())) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                    "name",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + product.getName());
        }
    }

    private void checkProductPriceActuality(Product product, Product productToBasket) throws OnlineShopException {
        if (product.getPrice() != (productToBasket.getPrice())) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                    "price",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + product.getPrice());
        }
    }

    private GetProductResponse createGetProductResponse(Product product) {
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCount(),
                null);
    }

    private Integer getBasketProductIdFromAddressLine(String number) throws OnlineShopException {
        Integer productId;
        try {
            productId = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/baskets/}");
        }
        return productId;
    }


}
