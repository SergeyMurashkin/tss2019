package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.*;
import net.thumbtack.onlineshop.daoImpl.*;
import net.thumbtack.onlineshop.dto.requests.PurchaseProductFromBasketRequest;
import net.thumbtack.onlineshop.dto.requests.PurchaseProductRequest;
import net.thumbtack.onlineshop.dto.responses.ConsolidatedStatementResponse;
import net.thumbtack.onlineshop.dto.responses.PurchaseProductFromBasketResponse;
import net.thumbtack.onlineshop.dto.responses.PurchaseProductResponse;
import net.thumbtack.onlineshop.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseService {

    private PurchaseDao purchaseDao = new PurchaseDaoImpl();
    private UserDao userDao = new UserDaoImpl();
    private ProductDao productDao = new ProductDaoImpl();
    private BasketDao basketDao = new BasketDaoImpl();
    private CategoryDao categoryDao = new CategoryDaoImpl();

    public PurchaseProductResponse purchaseProduct(PurchaseProductRequest request,
                                                   String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        Client client = userDao.getClient(user);
        Purchase purchase = createPurchase(request);
        purchase.setClientId(client.getId());
        Product product;
        try {
            product = productDao.getProduct(purchase.getProductId());
        } catch (OnlineShopException ex) {
            ex.setField("id");
            throw ex;
        }
        checkProductNameActuality(product, purchase);
        checkProductPriceActuality(product, purchase);
        checkProductCountSufficiency(product, purchase);
        checkClientDepositSufficiency(client, purchase);
        purchaseDao.purchaseProduct(client, product, purchase);
        PurchaseProductResponse response =
                new PurchaseProductResponse(purchase.getProductId(), purchase.getName(), purchase.getPrice(), purchase.getCount());
        return response;
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
        User user = userDao.getActualUser(cookieValue);
        Client client = userDao.getClient(user);

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

        List<Product> bought = new ArrayList<>();

        for (Product product : products) {
            Product basketProduct;
            try {
                basketProduct = basketDao.getBasketProduct(client, product.getId());
            } catch (OnlineShopException ex) {
                continue;
            }
            if (product.getCount() == 0 || product.getCount() > basketProduct.getCount()) {
                product.setCount(basketProduct.getCount());
            }

            Product marketProduct;
            try {
                marketProduct = productDao.getProduct(product.getId());
            } catch (OnlineShopException ex) {
                continue;
            }
            product.setVersion(marketProduct.getVersion());
            if (product.getName().equals(marketProduct.getName())
                    && product.getPrice() == (marketProduct.getPrice())
                    && marketProduct.getCount() >= product.getCount()) {
                bought.add(product);
            }

        }

        int totalCost = 0;
        for (Product boughtProduct : bought) {
            totalCost += boughtProduct.getCount() * boughtProduct.getPrice();
        }

        List<PurchaseProductResponse> boughtResponse = new ArrayList<>();
        if (totalCost <= client.getDeposit().getDeposit()) {
            List<Product> boughtProducts = purchaseDao.purchaseProductsFromBasket(client, bought);
            for (Product boughtProduct : boughtProducts) {
                boughtResponse.add(createPurchaseProductResponse(boughtProduct));
            }
        }
        List<Product> remainingProducts = basketDao.getClientBasket(client);
        List<PurchaseProductResponse> remainingResponses = new ArrayList<>();
        for (Product remainingProduct : remainingProducts) {
            remainingResponses.add(createPurchaseProductResponse(remainingProduct));
        }
        return new PurchaseProductFromBasketResponse(boughtResponse, remainingResponses);
    }


    public ConsolidatedStatementResponse getConsolidatedStatement(List<Integer> categoriesId, List<Integer> productsId, List<Integer> clientsId, String order, String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);

        if (categoriesId == null) {
            categoriesId = new ArrayList<>();
        }
        if (productsId == null) {
            productsId = new ArrayList<>();
        }
        if (clientsId == null) {
            clientsId = new ArrayList<>();
        }

        ConsolidatedStatementResponse response = new ConsolidatedStatementResponse();


        if (order.equals("category")) {
            response.setCategoriesId_totalPurchases(new HashMap<>());
            response.setCategoriesId_purchases(new HashMap<>());

            if (categoriesId.isEmpty()) {
                List<Integer> allCategoriesId = categoryDao.getAllCategoriesAndSubCategoriesId();
                //  purchases by all categories for all products and all clients
                if (productsId.isEmpty() && clientsId.isEmpty()) {
                    List<Purchase> purchases = purchaseDao.getPurchasesByWithoutCategoriesNoLimit();
                    List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
                    response.getCategoriesId_purchases().put(0, responses);
                    for (Integer categoryId : allCategoriesId) {
                        addByCategoryPurchasesToResponseNoLimit(response, categoryId);
                    }
                }
                //  purchases by all categories for list products and all clients
                if (!productsId.isEmpty() && clientsId.isEmpty()) {
                    addWithoutCategoryPurchasesToResponseLimitProducts(response, productsId);
                    for (Integer categoryId : allCategoriesId) {
                        addByCategoryPurchasesToResponseLimitProducts(response, categoryId, productsId);
                    }
                }
                //  purchases by all categories for all products and list clients
                if (productsId.isEmpty() && !clientsId.isEmpty()) {
                    addWithoutCategoryPurchasesToResponseLimitClients(response, clientsId);
                    for (Integer categoryId : allCategoriesId) {
                        addByCategoryPurchasesToResponseLimitClients(response, categoryId, clientsId);
                    }
                }
                //  purchases by all categories for list products and list clients
                if (!productsId.isEmpty() && !clientsId.isEmpty()) {
                    addWithoutCategoryPurchasesToResponseLimitProductsAndClients(response, productsId, clientsId);
                    for (Integer categoryId : allCategoriesId) {
                        addByCategoryPurchasesToResponseLimitProductsAndClients(response, categoryId, productsId, clientsId);
                    }
                }

            } else {
                if (!categoriesId.contains(0)) {

                    categoriesId.remove((Integer) 0);
                    // purchases by  without category for all products and all clients
                    if (productsId.isEmpty() && clientsId.isEmpty()) {
                        addWithoutCategoryPurchasesToResponseNoLimit(response);
                    }
                    // purchases by  without category  for list products and all clients
                    if (!productsId.isEmpty() && clientsId.isEmpty()) {
                        addWithoutCategoryPurchasesToResponseLimitProducts(response, productsId);
                    }
                    // purchases by  without category  for all products and list clients
                    if (productsId.isEmpty() && !clientsId.isEmpty()) {
                        addWithoutCategoryPurchasesToResponseLimitClients(response, clientsId);

                    }
                    // purchases by  without category  for list products and list clients
                    if (!productsId.isEmpty() && !clientsId.isEmpty()) {
                        addWithoutCategoryPurchasesToResponseLimitProductsAndClients(response, productsId, clientsId);
                    }

                }

                // purchases by list categories for all products and all clients
                if (productsId.isEmpty() && clientsId.isEmpty()) {
                    for (Integer categoryId : categoriesId) {
                        addByCategoryPurchasesToResponseNoLimit(response, categoryId);
                    }
                }
                // purchases by list categories for list products and all clients
                if (!productsId.isEmpty() && clientsId.isEmpty()) {
                    for (Integer categoryId : categoriesId) {
                        addByCategoryPurchasesToResponseLimitProducts(response, categoryId, productsId);
                    }
                }
                // purchases by list categories for all products and list clients
                if (productsId.isEmpty() && !clientsId.isEmpty()) {
                    for (Integer categoryId : categoriesId) {
                        addByCategoryPurchasesToResponseLimitClients(response, categoryId, clientsId);
                    }
                }
                // purchases by list categories for list products and list clients
                if (!productsId.isEmpty() && !clientsId.isEmpty()) {
                    for (Integer categoryId : categoriesId) {
                        addByCategoryPurchasesToResponseLimitProductsAndClients(response, categoryId, productsId, clientsId);
                    }
                }
            }
        }


        if (order.equals("product")) {
            response.setProductsId_totalPurchases(new HashMap<>());
            response.setProductsId_purchases(new HashMap<>());

            if (productsId.isEmpty()) {

                List<Integer> allProductsId = productDao.getAllProductsId();
                //  purchases by all products for all categories and all clients
                if (categoriesId.isEmpty() && clientsId.isEmpty()) {
                    for (Integer productId : allProductsId) {
                        addByProductPurchasesToResponseNoLimit(response, productId);
                    }
                }
                //  purchases by all products for list categories and all clients
                if (!categoriesId.isEmpty() && clientsId.isEmpty()) {
                    if (categoriesId.contains(0)) {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategoriesAndWithout(categoriesId);
                        for (Integer productId : productsIdByCategories) {
                            addByProductPurchasesToResponseNoLimit(response, productId);
                        }
                    }else {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategories(categoriesId);
                        for (Integer productId : productsIdByCategories) {
                            addByProductPurchasesToResponseNoLimit(response, productId);
                        }
                    }
                }
                //  purchases by all products for all categories and list clients
                if (categoriesId.isEmpty() && !clientsId.isEmpty()) {
                    for (Integer productId : allProductsId) {
                        addByProductPurchasesToResponseLimitClients(response, productId, clientsId);
                    }
                }
                // purchases by all products for list categories and list clients
                if (!categoriesId.isEmpty() && !clientsId.isEmpty()) {
                    if(categoriesId.contains(0)){
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategoriesAndWithout(categoriesId);
                        for (Integer productId : productsIdByCategories) {
                            addByProductPurchasesToResponseLimitClients(response, productId, clientsId);
                        }
                    }else {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategories(categoriesId);
                        for (Integer productId : productsIdByCategories) {
                            addByProductPurchasesToResponseLimitClients(response, productId, clientsId);
                        }
                    }
                }

            } else {

                //  purchases by list products for all categories and all clients
                if (categoriesId.isEmpty() && clientsId.isEmpty()) {
                    for (Integer productId : productsId) {
                        addByProductPurchasesToResponseNoLimit(response, productId);
                    }
                }
                // all purchases by list products for list categories and all clients
                if (!categoriesId.isEmpty() && clientsId.isEmpty()) {
                    if (categoriesId.contains(0)) {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategoriesAndWithout(categoriesId);
                        productsId.retainAll(productsIdByCategories);
                        for (Integer productId : productsId) {
                            addByProductPurchasesToResponseNoLimit(response, productId);
                        }
                    }else {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategories(categoriesId);
                        productsId.retainAll(productsIdByCategories);
                        for (Integer productId : productsId) {
                            addByProductPurchasesToResponseNoLimit(response, productId);
                        }
                    }
                }
                // all purchases by list products for all categories and list clients
                if (categoriesId.isEmpty() && !clientsId.isEmpty()) {
                    for (Integer productId : productsId) {
                        addByProductPurchasesToResponseLimitClients(response, productId, clientsId);
                    }
                }
                // all purchases by list products for list categories and list clients
                if (!categoriesId.isEmpty() && !clientsId.isEmpty()) {
                    if(categoriesId.contains(0)){
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategoriesAndWithout(categoriesId);
                        productsId.retainAll(productsIdByCategories);
                        for (Integer productId : productsId) {
                            addByProductPurchasesToResponseLimitClients(response, productId, clientsId);
                        }
                    }else {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategories(categoriesId);
                        productsId.retainAll(productsIdByCategories);
                        for (Integer productId : productsId) {
                            addByProductPurchasesToResponseLimitClients(response, productId, clientsId);
                        }
                    }
                }
            }


        }

        if (order.equals("client")) {
            response.setClientsId_totalPurchases(new HashMap<>());
            response.setClientsId_purchases(new HashMap<>());

            if (clientsId.isEmpty()) {
                List<Integer> allClientsId = userDao.getAllClientsId();
                //  purchases by all clients for all categories and all products
                if (categoriesId.isEmpty() && productsId.isEmpty()) {
                    for (Integer clientId : allClientsId) {
                        addByClientPurchasesToResponseNoLimit(response, clientId);
                    }
                }
                //  purchases by all clients for list categories and all products
                if (!categoriesId.isEmpty() && productsId.isEmpty()) {
                    if(categoriesId.contains(0)) {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategoriesAndWithout(categoriesId);
                        for (Integer clientId : allClientsId) {
                            addByClientPurchasesToResponseLimitProducts(response, clientId, productsIdByCategories);
                        }
                    } else{
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategories(categoriesId);
                        for (Integer clientId : allClientsId) {
                            addByClientPurchasesToResponseLimitProducts(response, clientId, productsIdByCategories);
                        }
                    }
                }
                //  purchases by all clients for all categories and list products
                if (categoriesId.isEmpty() && !productsId.isEmpty()) {
                    for (Integer clientId : allClientsId) {
                        addByClientPurchasesToResponseLimitProducts(response, clientId, productsId);
                    }
                }
                //  purchases by all clients for list categories and list products
                if (!categoriesId.isEmpty() && !productsId.isEmpty()) {
                    if(categoriesId.contains(0)) {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategoriesAndWithout(categoriesId);
                        productsId.retainAll(productsIdByCategories);
                        for (Integer clientId : allClientsId) {
                            addByClientPurchasesToResponseLimitProducts(response, clientId, productsId);
                        }
                    } else{
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategories(categoriesId);
                        productsId.retainAll(productsIdByCategories);
                        for (Integer clientId : allClientsId) {
                            addByClientPurchasesToResponseLimitProducts(response, clientId, productsId);
                        }
                    }
                }
            } else {

                //  purchases by all clients for all categories and all products
                if (categoriesId.isEmpty() && productsId.isEmpty()) {
                    for (Integer clientId : clientsId) {
                        addByClientPurchasesToResponseNoLimit(response, clientId);
                    }
                }
                //  purchases by all clients for list categories and all products
                if (!categoriesId.isEmpty() && productsId.isEmpty()) {
                    if(categoriesId.contains(0)) {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategoriesAndWithout(categoriesId);
                        for (Integer clientId : clientsId) {
                            addByClientPurchasesToResponseLimitProducts(response, clientId, productsIdByCategories);
                        }
                    } else{
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategories(categoriesId);
                        for (Integer clientId : clientsId) {
                            addByClientPurchasesToResponseLimitProducts(response, clientId, productsIdByCategories);
                        }
                    }
                }
                //  purchases by all clients for all categories and list products
                if (categoriesId.isEmpty() && !productsId.isEmpty()) {
                    for (Integer clientId : clientsId) {
                        addByClientPurchasesToResponseLimitProducts(response, clientId, productsId);
                    }
                }
                //  purchases by all clients for list categories and list products
                if (!categoriesId.isEmpty() && !productsId.isEmpty()) {
                    if(categoriesId.contains(0)) {
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategoriesAndWithout(categoriesId);
                        productsId.retainAll(productsIdByCategories);
                        for (Integer clientId : clientsId) {
                            addByClientPurchasesToResponseLimitProducts(response, clientId, productsId);
                        }
                    } else{
                        List<Integer> productsIdByCategories = productDao.getProductsIdByCategories(categoriesId);
                        productsId.retainAll(productsIdByCategories);
                        for (Integer clientId : clientsId) {
                            addByClientPurchasesToResponseLimitProducts(response, clientId, productsId);
                        }
                    }
                }
            }
        }
        return response;
    }

    private void addWithoutCategoryPurchasesToResponseNoLimit(ConsolidatedStatementResponse response) {
        List<Purchase> purchases = purchaseDao.getPurchasesByWithoutCategoriesNoLimit();
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getCategoriesId_purchases().put(0, responses);
    }

    private void addWithoutCategoryPurchasesToResponseLimitProducts(ConsolidatedStatementResponse response,
                                                                    List<Integer> productsId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByWithoutCategoriesLimitProducts(productsId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getCategoriesId_purchases().put(0, responses);
    }

    private void addWithoutCategoryPurchasesToResponseLimitClients(ConsolidatedStatementResponse response,
                                                                   List<Integer> clientsId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByWithoutCategoriesLimitClients(clientsId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getCategoriesId_purchases().put(0, responses);
    }

    private void addWithoutCategoryPurchasesToResponseLimitProductsAndClients(ConsolidatedStatementResponse response,
                                                                              List<Integer> productsId,
                                                                              List<Integer> clientsId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByWithoutCategoriesLimitProductsAndClients(productsId, clientsId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getCategoriesId_purchases().put(0, responses);
    }

    private void addByCategoryPurchasesToResponseNoLimit(ConsolidatedStatementResponse response,
                                                         Integer categoryId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByCategoryNoLimit(categoryId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getCategoriesId_purchases().put(categoryId, responses);
    }

    private void addByCategoryPurchasesToResponseLimitProducts(ConsolidatedStatementResponse response,
                                                               Integer categoryId,
                                                               List<Integer> productsId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByCategoryLimitProducts(categoryId, productsId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getCategoriesId_purchases().put(categoryId, responses);
    }

    private void addByCategoryPurchasesToResponseLimitClients(ConsolidatedStatementResponse response,
                                                              Integer categoryId,
                                                              List<Integer> clientsId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByCategoryLimitClients(categoryId, clientsId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getCategoriesId_purchases().put(categoryId, responses);
    }

    private void addByCategoryPurchasesToResponseLimitProductsAndClients(ConsolidatedStatementResponse response,
                                                                         Integer categoryId,
                                                                         List<Integer> productsId,
                                                                         List<Integer> clientsId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByCategoryLimitProductsAndClients(categoryId, productsId, clientsId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getCategoriesId_purchases().put(categoryId, responses);
    }

    private void addByProductPurchasesToResponseNoLimit(ConsolidatedStatementResponse response,
                                                        Integer productId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByProductNoLimit(productId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getProductsId_purchases().put(productId, responses);
    }

    private void addByProductPurchasesToResponseLimitClients(ConsolidatedStatementResponse response,
                                                                Integer productId,
                                                                List<Integer> clientsId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByProductLimitClients(productId,clientsId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getProductsId_purchases().put(productId, responses);
    }

    private void addByClientPurchasesToResponseNoLimit(ConsolidatedStatementResponse response,
                                                        Integer clientId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByClientNoLimit(clientId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getClientsId_purchases().put(clientId, responses);
    }

    private void addByClientPurchasesToResponseLimitProducts(ConsolidatedStatementResponse response,
                                                             Integer clientId,
                                                             List<Integer> productsId) {
        List<Purchase> purchases = purchaseDao.getPurchasesByClientLimitProducts(clientId,productsId);
        List<PurchaseProductResponse> responses = createPurchaseProductResponses(purchases);
        response.getClientsId_purchases().put(clientId, responses);
    }

    private PurchaseProductResponse createPurchaseProductResponse(Product product) {
        return new PurchaseProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCount());
    }

    private PurchaseProductResponse createPurchaseProductResponse(Purchase purchase) {
        return new PurchaseProductResponse(
                purchase.getProductId(),
                purchase.getName(),
                purchase.getPrice(),
                purchase.getCount());
    }

    private List<PurchaseProductResponse> createPurchaseProductResponses(List<Purchase> purchases) {
        List<PurchaseProductResponse> responses = new ArrayList<>();
        for (Purchase purchase : purchases) {
            responses.add(createPurchaseProductResponse(purchase));
        }
        return responses;
    }


    private void checkProductNameActuality(Product product, Purchase purchase) throws OnlineShopException {
        if (!product.getName().equals(purchase.getName())) {
            throw new OnlineShopException(
                    OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                    "name",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + product.getName());
        }
    }

    private void checkProductPriceActuality(Product product, Purchase purchase) throws OnlineShopException {
        if (product.getPrice() != (purchase.getPrice())) {
            throw new OnlineShopException(
                    OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                    "price",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + product.getPrice());
        }
    }

    private void checkProductCountSufficiency(Product product, Purchase purchase) throws OnlineShopException {
        if (product.getCount() < purchase.getCount()) {
            throw new OnlineShopException(
                    OnlineShopErrorCode.PRODUCT_INSUFFICIENT_AMOUNT,
                    "count",
                    OnlineShopErrorCode.PRODUCT_INSUFFICIENT_AMOUNT.getErrorText() + product.getCount());
        }
    }

    private void checkClientDepositSufficiency(Client client, Purchase purchase) throws OnlineShopException {
        if (client.getDeposit().getDeposit() < (purchase.getCount() * purchase.getPrice())) {
            throw new OnlineShopException(
                    OnlineShopErrorCode.DEPOSIT_INSUFFICIENT_AMOUNT,
                    null,
                    OnlineShopErrorCode.DEPOSIT_INSUFFICIENT_AMOUNT.getErrorText());
        }
    }

}
