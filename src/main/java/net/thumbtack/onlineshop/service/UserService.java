package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.daoImpl.UserDaoImpl;
import net.thumbtack.onlineshop.dto.requests.*;
import net.thumbtack.onlineshop.dto.responses.AdminRegistrationResponse;
import net.thumbtack.onlineshop.dto.responses.ClientRegistrationResponse;
import net.thumbtack.onlineshop.dto.responses.GetAllUsersResponse;
import net.thumbtack.onlineshop.model.*;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private UserDao userDao = new UserDaoImpl();

    public AdminRegistrationResponse registerAdmin(AdminRegistrationRequest request, String cookieValue) throws OnlineShopException {
        Admin admin = createAdmin(request);
        if (userDao.isLoginExists(admin.getLogin())){
            throw new OnlineShopException(OnlineShopErrorCode.USER_LOGIN_DUPLICATE,
                    "Login",
                    OnlineShopErrorCode.USER_LOGIN_DUPLICATE.getErrorText());
        }
        userDao.registerAdmin(admin);
        userDao.loginUser(admin.getLogin(), admin.getPassword(), cookieValue);
        return createAdminRegistrationResponse(admin);
    }

    public ClientRegistrationResponse registerClient(ClientRegistrationRequest request, String cookieValue) throws OnlineShopException {
        Client client = createClient(request);
        if (userDao.isLoginExists(client.getLogin())){
            throw new OnlineShopException(OnlineShopErrorCode.USER_LOGIN_DUPLICATE,
                    "Login",
                    OnlineShopErrorCode.USER_LOGIN_DUPLICATE.getErrorText());
        }
        userDao.registerClient(client);
        userDao.loginUser(client.getLogin(), client.getPassword(), cookieValue);
        return createClientRegistrationResponse(client);
    }

    public <T> T loginUser(LoginUserRequest request, String cookieValue) throws OnlineShopException {
        if(!userDao.isLoginExists(request.getLogin())){
            throw new OnlineShopException(OnlineShopErrorCode.USER_WRONG_LOGIN,
                    "Login",
                    OnlineShopErrorCode.USER_WRONG_LOGIN.getErrorText());
        }
        if(!userDao.isUserPasswordCorrect(request.getLogin(), request.getPassword())){
            throw new OnlineShopException(OnlineShopErrorCode.USER_WRONG_PASSWORD,
                    "Password",
                    OnlineShopErrorCode.USER_WRONG_PASSWORD.getErrorText());
        }
        userDao.loginUser(request.getLogin(), request.getPassword(), cookieValue);
        return getActualUser(cookieValue);
    }

    public String logoutUser(String cookieValue) {
        userDao.logoutUser(cookieValue);
        return "{}";
    }

    public <T> T getActualUser(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        } else {
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                Admin admin = userDao.getAdmin(user);
                return (T) createAdminRegistrationResponse(admin);
            }
            if (user.getUserType().equals(UserType.CLIENT.name())) {
                Client client = userDao.getClient(user);
                return (T) createClientRegistrationResponse(client);
            }
            throw new OnlineShopException(OnlineShopErrorCode.USER_ACCESS_PERMISSION,
                    null,
                    OnlineShopErrorCode.USER_ACCESS_PERMISSION.getErrorText());
        }
    }

    private Admin createAdmin(AdminRegistrationRequest request) {
        return new Admin(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                UserType.ADMIN.name(),
                request.getLogin(),
                request.getPassword(),
                request.getPosition());
    }

    private AdminRegistrationResponse createAdminRegistrationResponse(Admin admin) {
        return new AdminRegistrationResponse(admin.getId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getPatronymic(),
                admin.getPosition());
    }

    private Client createClient(ClientRegistrationRequest request) {
        return new Client(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                UserType.CLIENT.name(),
                request.getLogin(),
                request.getPassword(),
                request.getEmail(),
                request.getAddress(),
                request.getPhone(),
                new Deposit());
    }

    private ClientRegistrationResponse createClientRegistrationResponse(Client client) {
        return new ClientRegistrationResponse(client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPatronymic(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                client.getDeposit().getDeposit());
    }

    private Admin createAdmin(AdminProfileEditingRequest request) {
        return new Admin(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                UserType.ADMIN.name(),
                "",
                request.getNewPassword(),
                request.getPosition());
    }

    private Client createClient(ClientProfileEditingRequest request) {
        return new Client(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                UserType.CLIENT.name(),
                "",
                request.getNewPassword(),
                request.getEmail(),
                request.getAddress(),
                request.getPhone(),
                new Deposit());
    }

    public List<GetAllUsersResponse> getAllClients(String cookieValue) throws OnlineShopException {
        checkAdminPermission(cookieValue);
        List<GetAllUsersResponse> responses = new ArrayList<>();
        List<Client> clients = userDao.getAllClients();
        for (Client client : clients) {
            responses.add(createGetAllUsersResponse(client));
        }
        return responses;
    }

    private GetAllUsersResponse createGetAllUsersResponse(Client client) {
        return new GetAllUsersResponse(client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPatronymic(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone());
    }

    public AdminRegistrationResponse editAdminProfile(AdminProfileEditingRequest request, String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_WRONG_PASSWORD,
                    "Old password",
                    OnlineShopErrorCode.USER_WRONG_PASSWORD.getErrorText());
        }
        if (!user.getUserType().equals(UserType.ADMIN.name())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                    null,
                    OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
        }
        Admin newAdmin = createAdmin(request);
        newAdmin.setId(user.getId());
        userDao.editAdminProfile(newAdmin);
        Admin admin = userDao.getAdmin(newAdmin);
        return createAdminRegistrationResponse(admin);
    }

    public ClientRegistrationResponse editClientProfile(ClientProfileEditingRequest request, String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_WRONG_PASSWORD,
                    "Old password",
                    OnlineShopErrorCode.USER_WRONG_PASSWORD.getErrorText());
        }
        if (!user.getUserType().equals(UserType.CLIENT.name())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_CLIENT,
                    null,
                    OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());
        }
        Client newClient = createClient(request);
        newClient.setId(user.getId());
        userDao.editClientProfile(newClient);
        Client client = userDao.getClient(newClient);
        return createClientRegistrationResponse(client);
    }

    public ClientRegistrationResponse depositMoney(DepositMoneyRequest request, String cookieValue) throws OnlineShopException {
        Integer money;
        try {
            money = Integer.valueOf(request.getDeposit());
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_INCORRECT_VALUE,
                    "deposit",
                    OnlineShopErrorCode.DEPOSIT_INCORRECT_VALUE.getErrorText());
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
        Client beforeClient = userDao.getClient(user);
        userDao.depositMoney(beforeClient.getDeposit(), money);
        Client client = userDao.getClient(user);
        return createClientRegistrationResponse(client);
    }

    public ClientRegistrationResponse getBalance(String cookieValue) throws OnlineShopException {
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
        return createClientRegistrationResponse(client);
    }








    public void checkAdminPermission(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        }
        if (!user.getUserType().equals(UserType.ADMIN.name())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                    null,
                    OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
        }
    }

    public void checkAdminOrClientPermission(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        }
        if (!user.getUserType().equals(UserType.ADMIN.name()) && !user.getUserType().equals(UserType.CLIENT.name()) ) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_ACCESS_PERMISSION,
                    null,
                    OnlineShopErrorCode.USER_ACCESS_PERMISSION.getErrorText());
        }
    }

    public void checkClientPermission(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user == null) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                    null,
                    OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
        }
        if (!user.getUserType().equals(UserType.ADMIN.name())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_CLIENT,
                    null,
                    OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());
        }
    }
}
