package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.dao.PurchaseDao;
import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.daoImpl.ProductDaoImpl;
import net.thumbtack.onlineshop.daoImpl.PurchaseDaoImpl;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.dto.requests.PurchaseProductFromBasketRequest;
import net.thumbtack.onlineshop.dto.requests.PurchaseProductRequest;
import net.thumbtack.onlineshop.dto.responses.ConsolidatedStatementResponse;
import net.thumbtack.onlineshop.dto.responses.PurchaseProductFromBasketResponse;
import net.thumbtack.onlineshop.dto.responses.PurchaseProductResponse;
import net.thumbtack.onlineshop.model.*;

import java.util.ArrayList;
import java.util.List;

public class PurchaseService {

    private PurchaseDao purchaseDao = new PurchaseDaoImpl();
    private UserDao userDao = new UserDaoImpl();
    private ProductDao productDao = new ProductDaoImpl();

    public PurchaseProductRequest purchaseProduct(PurchaseProductRequest request,
                                                  String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        }
        if (!user.getUserType().equals(UserType.CLIENT.name())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_CLIENT,
                    null,
                    OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());
        }
        Purchase purchase = createPurchase(request);
        Client client = userDao.getClient(user);
        purchase.setClientId(client.getId());

        Product product = productDao.getProduct(purchase.getProductId());
        if (product == null) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "id",
                    OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());
        }
        if (!product.getName().equals(purchase.getName())) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                    "name",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + product.getName());
        }
        if (product.getPrice() != (purchase.getPrice())) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                    "price",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + product.getPrice());
        }
        if (product.getCount() < purchase.getCount()) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_INSUFFICIENT_AMOUNT,
                    "count",
                    OnlineShopErrorCode.PRODUCT_INSUFFICIENT_AMOUNT.getErrorText() + product.getCount());
        }
        if (client.getDeposit().getDeposit() < (purchase.getCount() * purchase.getPrice())) {
            throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_INSUFFICIENT_AMOUNT,
                    null,
                    OnlineShopErrorCode.DEPOSIT_INSUFFICIENT_AMOUNT.getErrorText());
        }
        purchaseDao.purchaseProduct(client, product, purchase);
        return request;
    }

    private Purchase createPurchase(PurchaseProductRequest request) {
        return new Purchase(0,
                0,
                request.getId(),
                request.getName(),
                request.getPrice(),
                request.getCount());
    }

    public PurchaseProductFromBasketResponse purchaseProductsFromBasket(List<PurchaseProductFromBasketRequest> requests,
                                                                        String cookieValue) throws OnlineShopException {
        List<Product> products = new ArrayList<>();
        for (PurchaseProductFromBasketRequest request : requests) {
            Product product = new Product(
                    request.getId(),
                    request.getName(),
                    request.getPrice(),
                    request.getCount() == null ? 0 : request.getCount(),
                    null,
                    null);
            products.add(product);
        }

        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        }
        if (!user.getUserType().equals(UserType.CLIENT.name())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_CLIENT,
                    null,
                    OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());
        }

        Client client = userDao.getClient(user);

        List<Product> bought = new ArrayList<>();

        List<Product> basketProducts = productDao.getClientBasket(client);
        for (Product product : products) {
            int index = basketProducts.indexOf(product);
            if (index != -1) {
                Product basketProduct = basketProducts.get(index);
                if (product.getCount() == 0 || product.getCount() > basketProduct.getCount()) {
                    product.setCount(basketProduct.getCount());
                }
                product.setVersion(basketProduct.getVersion());
                Product marketProduct = productDao.getProduct(product.getId());
                if (product.equals(marketProduct) && marketProduct.getCount() >= product.getCount()) {
                    bought.add(product);
                }
            }
        }

        int totalCost = 0;
        for (Product boughtProduct : bought) {
            totalCost += boughtProduct.getCount() * boughtProduct.getPrice();
        }


        List<Product> boughtProducts = purchaseDao.purchaseProductsFromBasket(client, bought, totalCost);
        List<PurchaseProductResponse> boughtResponse = new ArrayList<>();
        for (Product boughtProduct: boughtProducts){
            boughtResponse.add(createPurchaseProductResponse(boughtProduct));
        }
        List<Product> remainingProducts = productDao.getClientBasket(client);
        List<PurchaseProductResponse> remainingResponses = new ArrayList<>();
        for (Product remainingProduct: remainingProducts){
            remainingResponses.add(createPurchaseProductResponse(remainingProduct));
        }
        return new PurchaseProductFromBasketResponse(boughtResponse,remainingResponses);
    }





    public ConsolidatedStatementResponse getConsolidatedStatement(List<Integer> categoriesId, List<Integer> productsId, List<Integer> clientsId, String cookieValue) throws OnlineShopException {
        ConsolidatedStatementResponse response = purchaseDao.getConsolidatedStatement(categoriesId, productsId, clientsId);

        return null;
    }

    private PurchaseProductResponse createPurchaseProductResponse(Product product){
        return new PurchaseProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCount());
    }


}
