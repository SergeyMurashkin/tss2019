package net.thumbtack.onlineshop;


import com.google.gson.Gson;
import net.thumbtack.onlineshop.daoImpl.CategoryDaoImpl;
import net.thumbtack.onlineshop.daoImpl.ProductDaoImpl;
import net.thumbtack.onlineshop.daoImpl.PurchaseDaoImpl;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.dto.requests.*;
import net.thumbtack.onlineshop.dto.responses.AdminRegistrationResponse;
import net.thumbtack.onlineshop.dto.responses.ClientRegistrationResponse;
import net.thumbtack.onlineshop.models.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("api")
public class HelloController {

    private TokenGenerator tokenGenerator = new TokenGenerator();
    private Gson gson = new Gson();
    private UserDaoImpl userDao = new UserDaoImpl();
    private CategoryDaoImpl categoryDao = new CategoryDaoImpl();
    private ProductDaoImpl productDao = new ProductDaoImpl();
    private PurchaseDaoImpl purchaseDao = new PurchaseDaoImpl();


    @PostMapping("admins")
    public AdminRegistrationResponse adminRegistration(@RequestBody String requestBody,
                                                       HttpServletResponse response) {
        AdminRegistrationRequest request = gson.fromJson(requestBody, AdminRegistrationRequest.class);
        Admin admin = request.getAdminFromRequest();
        String cookieValue = tokenGenerator.generateToken();
        userDao.registerAdmin(admin, cookieValue);
        Cookie cookie = new Cookie("JAVASESSIONID", cookieValue);
        response.addCookie(cookie);
        return new AdminRegistrationResponse(admin.getId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getPatronymic(),
                admin.getPosition());
    }

    @PostMapping("clients")
    public ClientRegistrationResponse clientRegistration(@RequestBody String requestBody,
                                                         HttpServletResponse response) {
        ClientRegistrationRequest request = gson.fromJson(requestBody, ClientRegistrationRequest.class);
        Client client = request.getUserFromRequest();
        String cookieValue = tokenGenerator.generateToken();
        userDao.registerClient(client, cookieValue);
        Cookie cookie = new Cookie("JAVASESSIONID", cookieValue);
        response.addCookie(cookie);
        return new ClientRegistrationResponse(client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPatronymic(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                client.getDeposit());
    }

    @PostMapping("sessions")
    public <T> T loginUser(@RequestBody String requestBody,
                           HttpServletResponse response) {
        LoginUserRequest request = gson.fromJson(requestBody, LoginUserRequest.class);

        String cookieValue = tokenGenerator.generateToken();
        User user = userDao.loginUser(request.getLogin(), request.getPassword(), cookieValue);

        if (user.getUserType().equals(UserType.ADMIN.name())) {
            Admin admin = (Admin) user;
            Cookie cookie = new Cookie("JAVASESSIONID", cookieValue);
            response.addCookie(cookie);
            return (T) new AdminRegistrationResponse(admin.getId(),
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getPatronymic(),
                    admin.getPosition());
        }
        if (user.getUserType().equals(UserType.CLIENT.name())) {
            Client client = (Client) user;
            Cookie cookie = new Cookie("JAVASESSIONID", cookieValue);
            response.addCookie(cookie);
            return (T) new ClientRegistrationResponse(client.getId(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getPatronymic(),
                    client.getEmail(),
                    client.getAddress(),
                    client.getPhone(),
                    client.getDeposit());
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
    public <T> T getActualUser(@CookieValue("JAVASESSIONID") String cookieValue) {
        User user = userDao.getActualUser(cookieValue);
        if (user.getUserType().equals(UserType.ADMIN.name())) {
            Admin admin = (Admin) user;
            return (T) new AdminRegistrationResponse(admin.getId(),
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getPatronymic(),
                    admin.getPosition());
        }
        if (user.getUserType().equals(UserType.CLIENT.name())) {
            Client client = (Client) user;
            return (T) new ClientRegistrationResponse(client.getId(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getPatronymic(),
                    client.getEmail(),
                    client.getAddress(),
                    client.getPhone(),
                    client.getDeposit());
        } else {
            return null;
        }
    }


    @GetMapping("clients")
    public List<Client> getAllUsers(@CookieValue("JAVASESSIONID") String cookieValue) {
        return userDao.getAllClients(cookieValue);
    }

    @PutMapping("admins")
    public AdminRegistrationResponse adminProfileEditing(@CookieValue("JAVASESSIONID") String cookieValue,
                                                         @RequestBody String requestBody) {
        AdminProfileEditingRequest request = gson.fromJson(requestBody, AdminProfileEditingRequest.class);
        Admin newAdmin = request.createNewUser();
        Admin admin = userDao.adminProfileEditing(newAdmin, cookieValue, request.getOldPassword());
        return new AdminRegistrationResponse(admin.getId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getPatronymic(),
                admin.getPosition());
    }

    @PutMapping("clients")
    public ClientRegistrationResponse clientProfileEditing(@CookieValue("JAVASESSIONID") String cookieValue,
                                                           @RequestBody String requestBody) {
        ClientProfileEditingRequest request = gson.fromJson(requestBody, ClientProfileEditingRequest.class);
        Client newClient = request.createNewClient();
        Client client = userDao.clientProfileEditing(newClient, cookieValue, request.getOldPassword());
        return new ClientRegistrationResponse(client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPatronymic(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                client.getDeposit());
    }


    @PostMapping("categories")
    public Category addCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                                           @RequestBody String requestBody) {
        AddCategoryRequest request = gson.fromJson(requestBody, AddCategoryRequest.class);
        Category category = new Category();
        category.setName(request.getName());
        category.setParentId(request.getParentId());
        return categoryDao.addCategory(cookieValue, category);
    }

    @GetMapping("categories/{number}")
    public Category getCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                @PathVariable(name = "number") String number) {
        Integer id = Integer.valueOf(number);
        return categoryDao.getCategory(cookieValue, id);
    }

    @PutMapping("categories/{number}")
    public Category editCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                 @RequestBody String requestBody,
                                 @PathVariable(name = "number") String number) {
        AddCategoryRequest request = gson.fromJson(requestBody, AddCategoryRequest.class);
        Integer id = Integer.valueOf(number);
        Category category = new Category();
        category.setId(id);
        category.setName(request.getName());
        category.setParentId(request.getParentId());
        return categoryDao.editCategory(cookieValue, category);
    }

    @DeleteMapping("categories/{number}")
    public Category deleteCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                                 @PathVariable(name = "number") String number) {
        Integer id = Integer.valueOf(number);
        categoryDao.deleteCategory(cookieValue, id);
        return new Category();
    }

    @GetMapping("categories")
    public List<Category> getAllCategories(@CookieValue("JAVASESSIONID") String cookieValue) {
        return categoryDao.getAllCategories(cookieValue);
    }


    @PostMapping("products")
    public Product addProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                                @RequestBody String requestBody) {
        AddProductRequest request = gson.fromJson(requestBody, AddProductRequest.class);
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCount(request.getCount());
        List<Category> categories = new ArrayList<>();
        if(request.getCategories()!=null) {
            for (Integer id : request.getCategories()) {
                Category category = new Category();
                category.setId(id);
                categories.add(category);
            }
        }
        product.setCategories(categories);
        productDao.addProduct(cookieValue, product);
        return product;
    }


    @PutMapping("products/{number}")
    public Product editProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                              @RequestBody String requestBody,
                               @PathVariable(name = "number") String number) {
        AddProductRequest request = gson.fromJson(requestBody, AddProductRequest.class);
        Integer productId = Integer.valueOf(number);
        Product product = new Product();
        product.setId(productId);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCount(request.getCount());
        List<Category> categories = new ArrayList<>();
        for (Integer id : request.getCategories()) {
            Category category = new Category();
            category.setId(id);
            categories.add(category);
        }
        product.setCategories(categories);
        productDao.editProduct(cookieValue, product);
        return product;
    }

    @DeleteMapping("products/{number}")
    public Product deleteProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                               @PathVariable(name = "number") String number) {
        Integer id = Integer.valueOf(number);
        productDao.deleteProduct(cookieValue, id);
        return new Product() ;
    }

    @GetMapping("products/{number}")
    public Product getProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                                 @PathVariable(name = "number") String number) {
        Integer id = Integer.valueOf(number);
        return productDao.getProduct(cookieValue, id);
    }

    @GetMapping("products")
    public List<Product> getProductsByCategory(@CookieValue("JAVASESSIONID") String cookieValue,
                              @RequestParam(name = "category", required = false) List<Integer> categories,
                              @RequestParam(name = "order", defaultValue = "product", required = false) String  order) {

        return productDao.getProductsByCategory(cookieValue, categories, order);
    }

    @PutMapping("deposits")
    public ClientRegistrationResponse depositMoney(@CookieValue("JAVASESSIONID") String cookieValue,
                                         @RequestBody String requestBody) {
        DepositMoneyRequest deposit = gson.fromJson(requestBody, DepositMoneyRequest.class);
        Integer money = Integer.valueOf(deposit.getDeposit());
        System.out.println(money);
        Client client = userDao.depositMoney(cookieValue, money);
        return new ClientRegistrationResponse(client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPatronymic(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                client.getDeposit());
    }

    @GetMapping("deposits")
    public ClientRegistrationResponse getMoney(@CookieValue("JAVASESSIONID") String cookieValue) {
        Client client = userDao.getMoney(cookieValue);
        return new ClientRegistrationResponse(client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPatronymic(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                client.getDeposit());
    }

    @PostMapping("purchases")
    public PurchaseProductRequest purchaseProduct(@CookieValue("JAVASESSIONID") String cookieValue,
                                            @RequestBody String requestBody) {
        PurchaseProductRequest request = gson.fromJson(requestBody, PurchaseProductRequest.class);
        if(request.getCount()==null){
            request.setCount(1);
        }
        Purchase purchase = request.createPurchase();
        purchaseDao.purchaseProduct(cookieValue, purchase);
        return request;
    }


}

