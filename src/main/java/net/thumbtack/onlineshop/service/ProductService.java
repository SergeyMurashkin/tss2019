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

    public AddProductResponse addProduct(AddProductRequest request,
                                         String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
        Product product = createProduct(request);
        productDao.addProduct(product, request.getCategoriesId());
        Product addedProduct = productDao.getProduct(product.getId());
        return createAddProductResponse(addedProduct);
    }

    public AddProductResponse editProduct(EditProductRequest request,
                                          String number,
                                          String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
        Integer id = getProductIdFromAddressLine(number);
        Product oldProduct = productDao.getProduct(id);
        Product newProduct = createProduct(oldProduct, request);
        productDao.editProduct(newProduct, request.getCategoriesId());
        Product editedProduct = productDao.getProduct(id);
        return createAddProductResponse(editedProduct);
    }

    public String deleteProduct(String number,
                                String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
        Integer id = getProductIdFromAddressLine(number);
        productDao.deleteProduct(id);
        return "{}";
    }

    public GetProductResponse getProduct(String number,
                                         String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        checkAdminOrClientPermission(user);
        Integer id = getProductIdFromAddressLine(number);
        Product product = productDao.getProduct(id);
        return createGetProductResponse(product);
    }

    public List<GetProductResponse> getProductsByCategory(List<Integer> categoriesId,
                                                          String order,
                                                          String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        checkAdminOrClientPermission(user);
        List<Product> products;
        if (categoriesId == null) {
            if (order.equals("category")) {
                products = productDao.getAllProductsByCategoryOrder();
            } else {
                products = productDao.getAllProductsByProductOrder();
            }
        } else if (categoriesId.isEmpty()){
            products = productDao.getProductsWithoutCategories();
        } else {
            if (order.equals("category")) {
                products = productDao.getProductsByCategoryOrder(categoriesId);
            } else {
                products = productDao.getProductsByProductOrder(categoriesId);
            }
        }
        List<GetProductResponse> responses = new ArrayList<>();
        for (Product product : products) {
            responses.add(createGetProductResponse(product));
        }
        return responses;
    }

    private Product createProduct(AddProductRequest request) {
        return new Product(0,
                request.getName(),
                request.getPrice(),
                request.getCount(),
                new ArrayList<>(),
                null);
    }


    private AddProductResponse createAddProductResponse(Product product) {
        List<Integer> categoriesId = new ArrayList<>();
        for (Category category : product.getCategories()) {
            categoriesId.add(category.getId());
        }
        return new AddProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCount(),
                categoriesId);
    }


    private Product createProduct(Product oldProduct, EditProductRequest request) {
        return new Product(
                oldProduct.getId(),
                request.getName()==null?oldProduct.getName():request.getName(),
                request.getPrice()==null?oldProduct.getPrice():request.getPrice(),
                request.getCount()==null?oldProduct.getCount():request.getCount(),
                new ArrayList<>(),
                oldProduct.getVersion());
    }



    private GetProductResponse createGetProductResponse(Product product) {
        List<String> categoriesNames = new ArrayList<>();
        for (Category category : product.getCategories()) {
            categoriesNames.add(category.getName());
        }
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCount(),
                categoriesNames);
    }

    private Integer getProductIdFromAddressLine(String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/products/} ");
        }
        return id;
    }

    private void checkAdminOrClientPermission(User user) throws OnlineShopException {
        if (!user.getUserType().equals(UserType.ADMIN.name()) && !user.getUserType().equals(UserType.CLIENT.name())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_ACCESS_PERMISSION,
                    null,
                    OnlineShopErrorCode.USER_ACCESS_PERMISSION.getErrorText());
        }
    }






}
