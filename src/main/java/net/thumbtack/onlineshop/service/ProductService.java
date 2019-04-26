package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.ProductDao;
import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.daoImpl.ProductDaoImpl;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.dto.requests.AddProductRequest;
import net.thumbtack.onlineshop.dto.requests.EditProductRequest;
import net.thumbtack.onlineshop.dto.requests.PurchaseProductRequest;
import net.thumbtack.onlineshop.dto.responses.AddProductResponse;
import net.thumbtack.onlineshop.dto.responses.GetProductResponse;
import net.thumbtack.onlineshop.model.*;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private ProductDao productDao = new ProductDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    public AddProductResponse addProduct(AddProductRequest request) {
        Product product = createProduct(request);
        productDao.addProduct(product, request.getCategoriesId());
        Product addedProduct = productDao.getProduct(product.getId());
        return createAddProductResponse(addedProduct);
    }

    public AddProductResponse editProduct(EditProductRequest request, String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/products/} ");
        }
        Product oldProduct = productDao.getProduct(id);
        if(oldProduct==null) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());
        }
        Product newProduct = createProduct(id, request);
        newProduct.setVersion(oldProduct.getVersion());
        productDao.editProduct(newProduct, request.getCategoriesId());
        Product editedProduct = productDao.getProduct(id);
        return createAddProductResponse(editedProduct);
    }

    public String deleteProduct(String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/products/} ");
        }
        productDao.deleteProduct(id);
        return "{}";
    }

    public GetProductResponse getProduct(String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/products/} ");
        }
        Product product = productDao.getProduct(id);
        if (product == null) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number in address line",
                    OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());

        }
        return createGetProductResponse(product);
    }

    public List<GetProductResponse> getProductsByCategory(List<Integer> categoriesId, String order) {
        List<GetProductResponse> responses = new ArrayList<>();
        List<Product> products = productDao.getProductsByCategory(categoriesId, order);
        for (Product product : products) {
            responses.add(createGetProductResponse(product));
        }
        return responses;
    }

    public List<GetProductResponse> addProductInBasket(PurchaseProductRequest request, String cookieValue) throws OnlineShopException {

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

        Product productToBasket = createProduct(request);

        if (productDao.checkIsProductInClientBasket(client, productToBasket)) {
            throw new OnlineShopException(OnlineShopErrorCode.BASKET_PRODUCT_DUPLICATE,
                    "id",
                    OnlineShopErrorCode.BASKET_PRODUCT_DUPLICATE.getErrorText());
        }

        Product product = productDao.getProduct(productToBasket.getId());
        if (product == null) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "id",
                    OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());
        }
        if (!product.getName().equals(productToBasket.getName())) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                    "name",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + product.getName());
        }
        if (product.getPrice() != (productToBasket.getPrice())) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                    "price",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + product.getPrice());
        }

        productDao.addProductInBasket(client, productToBasket);

        List<Product> basketProducts = productDao.getClientBasket(client);

        List<GetProductResponse> responses = new ArrayList<>();
        for (Product productFromBasket : basketProducts) {
            responses.add(createGetProductResponse(productFromBasket));
        }
        return responses;
    }

    public String deleteProductFromBasket(String number, String cookieValue) throws OnlineShopException {
        Integer productId;
        try {
            productId = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/baskets/} ");
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
        productDao.deleteProductFromBasket(client, productId);
        return "{}";
    }

    public List<GetProductResponse> getClientBasket(String cookieValue) throws OnlineShopException {
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
        List<Product> basketProducts = productDao.getClientBasket(client);
        List<GetProductResponse> responses = new ArrayList<>();
        for (Product product : basketProducts) {
            responses.add(createGetProductResponse(product));
        }
        return responses;
    }

    public List<GetProductResponse> changeBasketProductQuantity(PurchaseProductRequest request, String cookieValue) throws OnlineShopException {
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

        Product newBasketProduct = createProduct(request);

        if (!productDao.checkIsProductInClientBasket(client, newBasketProduct)) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_IN_BASKET,
                    "id",
                    OnlineShopErrorCode.PRODUCT_NOT_IN_BASKET.getErrorText());
        }

        Product product = productDao.getBasketProduct(client, newBasketProduct);
        if (product == null) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "id",
                    OnlineShopErrorCode.PRODUCT_NOT_EXISTS.getErrorText());
        }
        if (!product.getName().equals(newBasketProduct.getName())) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_NAME,
                    "name",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_NAME.getErrorText() + product.getName());
        }
        if (product.getPrice() != (newBasketProduct.getPrice())) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE,
                    "price",
                    OnlineShopErrorCode.PRODUCT_ANOTHER_PRICE.getErrorText() + product.getPrice());
        }

        List<Product> basketProducts = productDao.changeBasketProductQuantity(client, newBasketProduct);
        List<GetProductResponse> responses = new ArrayList<>();
        for (Product productFromBasket : basketProducts) {
            responses.add(createGetProductResponse(productFromBasket));
        }
        return responses;
    }

    private Product createProduct(AddProductRequest request){
        return new Product(0,
                request.getName(),
                request.getPrice(),
                request.getCount(),
                new ArrayList<>(),
                null);
    }



    private AddProductResponse createAddProductResponse(Product product) {
        List<Integer> categoriesId = new ArrayList<>();
        for (Category category: product.getCategories()) {
            categoriesId.add(category.getId());
        }
        return new AddProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCount(),
                categoriesId);
    }


    private Product createProduct(Integer id, EditProductRequest request){
        return new Product(id,
                request.getName(),
                request.getPrice(),
                request.getCount(),
                new ArrayList<>(),
                null);
    }

    private Product createProduct(PurchaseProductRequest request){
        return new Product(
                request.getId(),
                request.getName(),
                request.getPrice(),
                request.getCount(),
                new ArrayList<>(),
                null);
    }

    private GetProductResponse createGetProductResponse(Product product) {
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCount());
    }

}
