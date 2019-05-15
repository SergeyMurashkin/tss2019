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

    public AdminRegistrationResponse registerAdmin(AdminRegistrationRequest request,
                                                   String cookieValue) throws OnlineShopException {
        Admin admin = createAdmin(request);
        userDao.registerAdmin(admin, cookieValue);
        Admin registeredAdmin = userDao.getAdmin(admin);
        return createAdminRegistrationResponse(registeredAdmin);
    }

    public ClientRegistrationResponse registerClient(ClientRegistrationRequest request,
                                                     String cookieValue) throws OnlineShopException {
        Client client = createClient(request);
        userDao.registerClient(client, cookieValue);
        Client registeredClient = userDao.getClient(client);
        return createClientRegistrationResponse(registeredClient);
    }

    public <T> T loginUser(LoginUserRequest request,
                           String cookieValue) throws OnlineShopException {
        User user = userDao.getUserByLoginAndPassword(request.getLogin(), request.getPassword());
        userDao.loginUser(user, cookieValue);
        return getActualUser(cookieValue);
    }

    public String logoutUser(String cookieValue) {
        userDao.logoutUser(cookieValue);
        return "{}";
    }

    public <T> T getActualUser(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        if (user.getUserType().equals(UserType.ADMIN.name())) {
            return (T) createAdminRegistrationResponse(userDao.getAdmin(user));
        }
        if (user.getUserType().equals(UserType.CLIENT.name())) {
            return (T) createClientRegistrationResponse(userDao.getClient(user));
        }
        throw new OnlineShopException(OnlineShopErrorCode.USER_ACCESS_PERMISSION,
                null,
                OnlineShopErrorCode.USER_ACCESS_PERMISSION.getErrorText());

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
                null,
                request.getNewPassword(),
                request.getPosition());
    }

    private Client createClient(ClientProfileEditingRequest request) {
        return new Client(request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                UserType.CLIENT.name(),
                null,
                request.getNewPassword(),
                request.getEmail(),
                request.getAddress(),
                request.getPhone(),
                null);
    }

    public List<GetAllUsersResponse> getAllClients(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        userDao.getAdmin(user);
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

    public AdminRegistrationResponse editAdminProfile(AdminProfileEditingRequest request,
                                                      String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        Admin admin = userDao.getAdmin(user);
        checkUserOldPasswordCorrectness(admin,request);
        Admin newAdmin = createAdmin(request);
        newAdmin.setId(admin.getId());
        userDao.editAdminProfile(newAdmin);
        admin = userDao.getAdmin(newAdmin);
        return createAdminRegistrationResponse(admin);
    }

    public ClientRegistrationResponse editClientProfile(ClientProfileEditingRequest request,
                                                        String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        Client client = userDao.getClient(user);
        checkUserOldPasswordCorrectness(client, request);
        Client newClient = createClient(request);
        newClient.setId(user.getId());
        userDao.editClientProfile(newClient);
        client = userDao.getClient(newClient);
        return createClientRegistrationResponse(client);
    }

     public ClientRegistrationResponse depositMoney(DepositMoneyRequest request,
                                                   String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        Client beforeClient = userDao.getClient(user);
        Integer additionalDeposit = getDepositFromRequest(request);
        userDao.depositMoney(beforeClient.getDeposit(), additionalDeposit);
        Client client = userDao.getClient(user);
        return createClientRegistrationResponse(client);
    }

    public ClientRegistrationResponse getBalance(String cookieValue) throws OnlineShopException {
        User user = userDao.getActualUser(cookieValue);
        Client client = userDao.getClient(user);
        return createClientRegistrationResponse(client);
    }


    private void checkUserOldPasswordCorrectness(User user,
                                                 AdminProfileEditingRequest request) throws OnlineShopException {
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_WRONG_PASSWORD,
                    "Old password",
                    OnlineShopErrorCode.USER_WRONG_PASSWORD.getErrorText());
        }
    }

    private void checkUserOldPasswordCorrectness(User user,
                                                 ClientProfileEditingRequest request) throws OnlineShopException {
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new OnlineShopException(OnlineShopErrorCode.USER_WRONG_PASSWORD,
                    "Old password",
                    OnlineShopErrorCode.USER_WRONG_PASSWORD.getErrorText());
        }
    }


    private Integer getDepositFromRequest(DepositMoneyRequest request) throws OnlineShopException {
        Integer money;
        try {
            money = Integer.valueOf(request.getDeposit());
        } catch (NumberFormatException ex) {
            throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_INCORRECT_VALUE,
                    "deposit",
                    OnlineShopErrorCode.DEPOSIT_INCORRECT_VALUE.getErrorText());
        }
        return money;
    }

}
