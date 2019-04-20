package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.TokenGenerator;
import net.thumbtack.onlineshop.daoImpl.CategoryDaoImpl;
import net.thumbtack.onlineshop.daoImpl.ProductDaoImpl;
import net.thumbtack.onlineshop.daoImpl.PurchaseDaoImpl;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.dto.requests.*;
import net.thumbtack.onlineshop.dto.responses.AdminRegistrationResponse;
import net.thumbtack.onlineshop.dto.responses.ClientRegistrationResponse;
import net.thumbtack.onlineshop.dto.responses.ConsolidatedStatementResponse;
import net.thumbtack.onlineshop.dto.responses.PurchaseProductFromBasketResponse;
import net.thumbtack.onlineshop.model.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class RequestController {

    private TokenGenerator tokenGenerator = new TokenGenerator();
    private UserDaoImpl userDao = new UserDaoImpl();
    private CategoryDaoImpl categoryDao = new CategoryDaoImpl();
    private ProductDaoImpl productDao = new ProductDaoImpl();
    private PurchaseDaoImpl purchaseDao = new PurchaseDaoImpl();

    @PostMapping("admins")
    public AdminRegistrationResponse adminRegistration(@Valid @RequestBody AdminRegistrationRequest request,
                                                       HttpServletResponse response) {
    	// REVU move code to service
    	Admin admin = new Admin(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                UserType.ADMIN.name(),
                request.getLogin(),
                request.getPassword(),
                request.getPosition());
        String cookieValue = tokenGenerator.generateToken();
        userDao.registerAdmin(admin, cookieValue);
        Cookie cookie = new Cookie("JAVASESSIONID", cookieValue);
        response.addCookie(cookie);
        return new AdminRegistrationResponse(admin);
    }

    @PostMapping("clients")
    public ClientRegistrationResponse clientRegistration(@Valid @RequestBody ClientRegistrationRequest request,
                                                         HttpServletResponse response) {
    	// REVU move code to service
        Client client = new Client(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                UserType.CLIENT.name(),
                request.getLogin(),
                request.getPassword(),
                request.getEmail(),
                request.getAddress(),
                request.getPhone(),
                // REVU create constructor without Deposit parameter, let it makes new Deposit() inside
                new Deposit());
        String cookieValue = tokenGenerator.generateToken();
        userDao.registerClient(client, cookieValue);
        // REVU magic constant "JAVASESSIONID", create static final field
        Cookie cookie = new Cookie("JAVASESSIONID", cookieValue);
        response.addCookie(cookie);
        return new ClientRegistrationResponse(client);
    }

    @PostMapping("sessions")
    public <T> T loginUser(@RequestBody LoginUserRequest request,
                           HttpServletResponse response) throws OnlineShopException {

        String cookieValue = tokenGenerator.generateToken();
        User user = userDao.loginUser(request.getLogin(), request.getPassword(), cookieValue);
        if (user.getUserType().equals(UserType.ADMIN.name())) {
            Admin admin = (Admin) user;
            Cookie cookie = new Cookie("JAVASESSIONID", cookieValue);
            response.addCookie(cookie);
            return (T) new AdminRegistrationResponse(admin);
        }
        if (user.getUserType().equals(UserType.CLIENT.name())) {
            Client client = (Client) user;
            Cookie cookie = new Cookie("JAVASESSIONID", cookieValue);
            response.addCookie(cookie);
            return (T) new ClientRegistrationResponse(client);
        } else {
            return null;
        }

    }

    @DeleteMapping("sessions")
    public String logoutUser(@CookieValue("JAVASESSIONID") String cookieValue) {
        userDao.logoutUser(cookieValue);
        return "{}";
    }


    @GetMapping("accounts")
    public <T> T getActualUser(@CookieValue("JAVASESSIONID") String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        } else {
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                Admin admin = (Admin) user;
                return (T) new AdminRegistrationResponse(admin);
            }
            if (user.getUserType().equals(UserType.CLIENT.name())) {
                Client client = (Client) user;
                return (T) new ClientRegistrationResponse(client);
            }
        }
        return (T) "{}";
    }


    @GetMapping("clients")
    public List<Client> getAllUsers(@CookieValue("JAVASESSIONID") String cookieValue) throws OnlineShopException {
        return userDao.getAllClients(cookieValue);
    }

    @PutMapping("admins")
    public AdminRegistrationResponse adminProfileEditing(@CookieValue("JAVASESSIONID") String cookieValue,
                                                         @RequestBody AdminProfileEditingRequest request) throws OnlineShopException {
        Admin newAdmin = request.createNewUser();
        Admin admin = userDao.adminProfileEditing(newAdmin, cookieValue, request.getOldPassword());
        return new AdminRegistrationResponse(admin);
    }

    @PutMapping("clients")
    public ClientRegistrationResponse clientProfileEditing(@CookieValue("JAVASESSIONID") String cookieValue,
                                                           @RequestBody ClientProfileEditingRequest request) throws OnlineShopException {
        Client newClient = request.createNewClient();
        Client client = userDao.clientProfileEditing(newClient, cookieValue, request.getOldPassword());
        return new ClientRegistrationResponse(client);
    }


    @PostMapping("categories")
    public Category addCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                @RequestBody AddCategoryRequest request) throws OnlineShopException {
        Category category = new Category(0, request.getName(), request.getParentId());
        return categoryDao.addCategory(cookieValue, category);
    }

    @GetMapping("categories/{number}")
    // REVU why not int number and catch exception in ExceptionGlobalController ? 
    public Category getCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                @PathVariable(name = "number") String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                    "number of category in address line",
                    "Use numbers after {api/categories/} ");
        }
        // REVU do not pass cookie value to all methods
        return categoryDao.getCategory(cookieValue, id);
    }

    @PutMapping("categories/{number}")
    public Category editCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                 @RequestBody AddCategoryRequest request,
                                 @PathVariable(name = "number") String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                    "number of category in address line",
                    "Use numbers after {api/categories/} ");
        }
        Category category = new Category(id, request.getName(), request.getParentId());
        return categoryDao.editCategory(cookieValue, category);
    }

    @DeleteMapping("categories/{number}")
    public String deleteCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                 @PathVariable(name = "number") String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                    "number of category in address line",
                    "Use numbers after {api/categories/} ");
        }
        categoryDao.deleteCategory(cookieValue, id);
        return "{}";
    }

    @GetMapping("categories")
    public List<Category> getAllCategories(@CookieValue("JAVASESSIONID") String cookieValue) throws OnlineShopException {
        return categoryDao.getAllCategories(cookieValue);
    }

    @PostMapping("products")
    public Product addProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                              @RequestBody AddProductRequest request) throws OnlineShopException {
        Product product = new Product(0, request.getName(), request.getPrice(), request.getCount(), new ArrayList<>());
        productDao.addProduct(cookieValue, product, request.getCategoriesId());
        return product;
    }


    @PutMapping("products/{number}")
    public Product editProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                               @RequestBody EditProductRequest request,
                               @PathVariable(name = "number") String number) throws OnlineShopException {
        Integer productId;
        try {
            productId = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/products/} ");
        }
        Product product = new Product(productId, request.getName(), request.getPrice(), request.getCount(), new ArrayList<>());
        productDao.editProduct(cookieValue, product, request.getCategoriesId());
        return product;
    }

    @DeleteMapping("products/{number}")
    public String  deleteProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                                 @PathVariable(name = "number") String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/products/} ");
        }
        productDao.deleteProduct(cookieValue, id);
        return "{}";
    }

    @GetMapping("products/{number}")
    public Product getProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                              @PathVariable(name = "number") String number) throws OnlineShopException {
        Integer id;
        try {
            id = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/products/} ");
        }
        return productDao.getProduct(cookieValue, id);
    }

    @GetMapping("products")
    public List<Product> getProductsByCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                               @RequestParam(name = "category", required = false) List<Integer> categoriesId,
                                               @RequestParam(name = "order", defaultValue = "product", required = false) String order) throws OnlineShopException {
         return productDao.getProductsByCategory(cookieValue, categoriesId, order);
    }

    @PutMapping("deposits")
    public ClientRegistrationResponse depositMoney(@CookieValue("JAVASESSIONID") String cookieValue,
                                                   @RequestBody DepositMoneyRequest deposit) throws OnlineShopException {
        Integer money;
        try {
           money = Integer.valueOf(deposit.getDeposit());
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_INCORRECT_VALUE,
                    "deposit",
                    OnlineShopErrorCode.DEPOSIT_INCORRECT_VALUE.getErrorText());
        }
        Client client = userDao.depositMoney(cookieValue, money);
        return new ClientRegistrationResponse(client);
    }

    @GetMapping("deposits")
    public ClientRegistrationResponse getMoney(@CookieValue("JAVASESSIONID") String cookieValue) throws OnlineShopException {
        Client client = userDao.getMoney(cookieValue);
        return new ClientRegistrationResponse(client);
    }

    @PostMapping("purchases")
    public PurchaseProductRequest purchaseProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                                                  @RequestBody PurchaseProductRequest request) throws OnlineShopException {
        Purchase purchase = new Purchase(0,
                0,
                request.getId(),
                request.getName(),
                request.getPrice(),
                request.getCount());
        purchaseDao.purchaseProduct(cookieValue, purchase);
        return request;
    }


    @PostMapping("baskets")
    public List<Product> addProductInBasket(@CookieValue("JAVASESSIONID") String cookieValue,
                                                  @Valid @RequestBody PurchaseProductRequest request) throws OnlineShopException {
        Product basketProduct = new Product(
                request.getId(),
                request.getName(),
                request.getPrice(),
                request.getCount(),
                new ArrayList<>());
        return productDao.addProductInBasket(cookieValue, basketProduct);
    }

    @DeleteMapping("baskets/{number}")
    public String  deleteProductFromBasket(@CookieValue("JAVASESSIONID") String cookieValue,
                                 @PathVariable(name = "number") String number) throws OnlineShopException {
        Integer productId;
        try {
            productId = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.PRODUCT_NOT_EXISTS,
                    "number of product in address line",
                    "Use numbers after {api/baskets/} ");
        }
        productDao.deleteProductFromBasket(cookieValue, productId);
        return "{}";
    }

    @PutMapping("baskets")
    public List<Product> changeProductQuantity (@CookieValue("JAVASESSIONID") String cookieValue,
                                         @Valid @RequestBody PurchaseProductRequest request) throws OnlineShopException {
        Product newBasketProduct = new Product(
                request.getId(),
                request.getName(),
                request.getPrice(),
                request.getCount(),
                new ArrayList<>());
        return productDao.changeProductQuantity(cookieValue, newBasketProduct);
    }

    @GetMapping("baskets")
    public List<Product> getClientBasket (@CookieValue("JAVASESSIONID") String cookieValue) throws OnlineShopException {
        return productDao.getClientBasket(cookieValue);
    }

    @PostMapping("purchases/baskets")
    public PurchaseProductFromBasketResponse purchaseProductsFromBasket(@CookieValue("JAVASESSIONID") String cookieValue,
                                                                        @RequestBody List<PurchaseProductFromBasketRequest> requests) throws OnlineShopException {
        List<Product> products = new ArrayList<>();
        for (PurchaseProductFromBasketRequest request : requests) {
            Product product = new Product(
                    request.getId(),
                    request.getName(),
                    request.getPrice(),
                    request.getCount(),
                    null);
            products.add(product);
        }
        return purchaseDao.purchaseProductsFromBasket(cookieValue, products);
    }

    @GetMapping("purchases")
    public ConsolidatedStatementResponse getConsolidatedStatement(@CookieValue("JAVASESSIONID") String cookieValue,
                                                                  @RequestParam(name = "category", required = false) List<Integer> categoriesId,
                                                                  @RequestParam(name = "product", required = false) List<Integer> productsId,
                                                                  @RequestParam(name = "client", required = false) List<Integer> clientsId) throws OnlineShopException {
        ConsolidatedStatementResponse response = purchaseDao.getConsolidatedStatement(categoriesId,productsId,clientsId);

        return null;
    }


}

