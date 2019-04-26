package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.TokenGenerator;
import net.thumbtack.onlineshop.dto.requests.*;
import net.thumbtack.onlineshop.dto.responses.*;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.service.CategoryService;
import net.thumbtack.onlineshop.service.ProductService;
import net.thumbtack.onlineshop.service.PurchaseService;
import net.thumbtack.onlineshop.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
public class RequestController {

    private UserService userService = new UserService();
    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private PurchaseService purchaseService = new PurchaseService();

    private TokenGenerator tokenGenerator = new TokenGenerator();

    @PostMapping("admins")
    public AdminRegistrationResponse adminRegistration(@Valid @RequestBody AdminRegistrationRequest request,
                                                       HttpServletResponse httpResponse) throws OnlineShopException {
        String cookieValue = tokenGenerator.generateToken();
        AdminRegistrationResponse response = userService.registerAdmin(request, cookieValue);
        Cookie cookie = new Cookie(OnlineShopServer.COOKIE_JAVASESSIONID, cookieValue);
        httpResponse.addCookie(cookie);
        return response;
    }

    @PostMapping("clients")
    public ClientRegistrationResponse clientRegistration(@Valid @RequestBody ClientRegistrationRequest request,
                                                         HttpServletResponse httpResponse) throws OnlineShopException {
        String cookieValue = tokenGenerator.generateToken();
        ClientRegistrationResponse response = userService.registerClient(request, cookieValue);
        Cookie cookie = new Cookie(OnlineShopServer.COOKIE_JAVASESSIONID, cookieValue);
        httpResponse.addCookie(cookie);
        return response;
    }

    @PostMapping("sessions")
    public <T> T loginUser(@Valid @RequestBody LoginUserRequest request,
                           HttpServletResponse httpResponse) throws OnlineShopException {
        String cookieValue = tokenGenerator.generateToken();
        T response = userService.loginUser(request, cookieValue);
        Cookie cookie = new Cookie(OnlineShopServer.COOKIE_JAVASESSIONID, cookieValue);
        httpResponse.addCookie(cookie);
        return response;
    }

    @DeleteMapping("sessions")
    public String logoutUser(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue) {
        return userService.logoutUser(cookieValue);
    }

    @GetMapping("accounts")
    public <T> T getActualUser(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue) throws OnlineShopException {
        return userService.getActualUser(cookieValue);
    }

    @GetMapping("clients")
    public List<GetAllUsersResponse> getAllUsers(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue) throws OnlineShopException {
        return userService.getAllClients(cookieValue);
    }

    @PutMapping("admins")
    public AdminRegistrationResponse adminProfileEditing(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                         @Valid @RequestBody AdminProfileEditingRequest request) throws OnlineShopException {
        return userService.editAdminProfile(request, cookieValue);
    }

    @PutMapping("clients")
    public ClientRegistrationResponse editClientProfile(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                        @Valid @RequestBody ClientProfileEditingRequest request) throws OnlineShopException {
        return userService.editClientProfile(request, cookieValue);
    }

    @PostMapping("categories")
    public AddCategoryResponse addCategory(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                           @Valid @RequestBody AddCategoryRequest request) throws OnlineShopException {
        userService.checkAdminPermission(cookieValue);
        return categoryService.addCategory(request);
    }

    @GetMapping("categories/{number}")
    public AddCategoryResponse getCategory(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                           @PathVariable(name = "number") String number) throws OnlineShopException {
        userService.checkAdminPermission(cookieValue);
        return categoryService.getCategory(number);
    }

    @PutMapping("categories/{number}")
    public AddCategoryResponse editCategory(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                            @Valid @RequestBody EditCategoryRequest request,
                                            @PathVariable(name = "number") String number) throws OnlineShopException {
        userService.checkAdminPermission(cookieValue);
        return categoryService.editCategory(request, number);
    }

    @DeleteMapping("categories/{number}")
    public String deleteCategory(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                 @PathVariable(name = "number") String number) throws OnlineShopException {
        userService.checkAdminPermission(cookieValue);
        return categoryService.deleteCategory(number);
    }

    @GetMapping("categories")
    public List<AddCategoryResponse> getAllCategories(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue) throws OnlineShopException {
        userService.checkAdminPermission(cookieValue);
        return categoryService.getAllCategories();
    }

    @PostMapping("products")
    public AddProductResponse addProduct(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                         @Valid @RequestBody AddProductRequest request) throws OnlineShopException {
        userService.checkAdminPermission(cookieValue);
        categoryService.checkCategories(request.getCategoriesId());
        return productService.addProduct(request);
    }

    @PutMapping("products/{number}")
    public AddProductResponse editProduct(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                          @Valid @RequestBody EditProductRequest request,
                                          @PathVariable(name = "number") String number) throws OnlineShopException {
        userService.checkAdminPermission(cookieValue);
        categoryService.checkCategories(request.getCategoriesId());
        return productService.editProduct(request, number);
    }

    @DeleteMapping("products/{number}")
    public String deleteProduct(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                @PathVariable(name = "number") String number) throws OnlineShopException {
        userService.checkAdminPermission(cookieValue);
        return productService.deleteProduct(number);
    }

    @GetMapping("products/{number}")
    public GetProductResponse getProduct(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                         @PathVariable(name = "number") String number) throws OnlineShopException {
        userService.checkAdminOrClientPermission(cookieValue);
        return productService.getProduct(number);
    }

    @GetMapping("products")
    public List<GetProductResponse> getProductsByCategory(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                          @RequestParam(name = "category", required = false) List<Integer> categoriesId,
                                                          @RequestParam(name = "order", defaultValue = "product", required = false) String order) throws OnlineShopException {
        userService.checkAdminOrClientPermission(cookieValue);
        return productService.getProductsByCategory(categoriesId, order);
    }

    @PutMapping("deposits")
    public ClientRegistrationResponse depositMoney(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                   @RequestBody DepositMoneyRequest request) throws OnlineShopException {
        return userService.depositMoney(request, cookieValue);
    }

    @GetMapping("deposits")
    public ClientRegistrationResponse getMoney(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue) throws OnlineShopException {
        return userService.getBalance(cookieValue);
    }

    @PostMapping("purchases")
    public PurchaseProductRequest purchaseProduct(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                  @Valid @RequestBody PurchaseProductRequest request) throws OnlineShopException {
        return purchaseService.purchaseProduct(request, cookieValue);
    }

    @PostMapping("baskets")
    public List<GetProductResponse> addProductInBasket(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                       @Valid @RequestBody PurchaseProductRequest request) throws OnlineShopException {
        return productService.addProductInBasket(request, cookieValue);
    }

    @DeleteMapping("baskets/{number}")
    public String deleteProductFromBasket(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                          @PathVariable(name = "number") String number) throws OnlineShopException {
        return productService.deleteProductFromBasket(number, cookieValue);
    }

    @PutMapping("baskets")
    public List<GetProductResponse> changeBasketProductQuantity(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                          @Valid @RequestBody PurchaseProductRequest request) throws OnlineShopException {
        return productService.changeBasketProductQuantity(request, cookieValue);
    }

    @GetMapping("baskets")
    public List<GetProductResponse> getClientBasket(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue) throws OnlineShopException {
        return productService.getClientBasket(cookieValue);
    }


    @PostMapping("purchases/baskets")
    public PurchaseProductFromBasketResponse purchaseProductsFromBasket(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                                        @RequestBody List<PurchaseProductFromBasketRequest> requests) throws OnlineShopException {
        return purchaseService.purchaseProductsFromBasket(requests, cookieValue);
    }









///////////   for think ...

    @GetMapping("purchases")
    public ConsolidatedStatementResponse getConsolidatedStatement(@CookieValue(OnlineShopServer.COOKIE_JAVASESSIONID) String cookieValue,
                                                                  @RequestParam(name = "category", required = false) List<Integer> categoriesId,
                                                                  @RequestParam(name = "product", required = false) List<Integer> productsId,
                                                                  @RequestParam(name = "client", required = false) List<Integer> clientsId) throws OnlineShopException {
        return purchaseService.getConsolidatedStatement(categoriesId, productsId, clientsId, cookieValue);
    }


}

