package net.thumbtack.onlineshop.testService;

import net.thumbtack.onlineshop.dto.requests.*;
import net.thumbtack.onlineshop.dto.responses.AdminRegistrationResponse;
import net.thumbtack.onlineshop.dto.responses.ClientRegistrationResponse;
import net.thumbtack.onlineshop.dto.responses.GetAllUsersResponse;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestUserService extends TestBaseService {

    @Test
    public void testAdminRegistration() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest request = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        AdminRegistrationResponse response = userService.registerAdmin(request, token);
        Assert.assertNotNull(response);
        Assert.assertNotEquals(0, response.getId());
        Assert.assertEquals(request.getFirstName(), response.getFirstName());
        Assert.assertEquals(request.getLastName(), response.getLastName());
        Assert.assertEquals(request.getPatronymic(), response.getPatronymic());
        Assert.assertEquals(request.getPosition(), response.getPosition());

        AdminRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(response, getActualUserResponse);
    }

    @Test
    public void testAdminRegistrationWithLoginDuplicate() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest request = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(request, token);
        OnlineShopException e=null;
        try {
            userService.registerAdmin(request, token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_LOGIN_DUPLICATE, e.getErrorCode());

    }

    @Test
    public void testClientRegistration() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        ClientRegistrationResponse response = userService.registerClient(request, token);
        Assert.assertNotNull(response);
        Assert.assertNotEquals(0, response.getId());
        Assert.assertEquals(request.getFirstName(), response.getFirstName());
        Assert.assertEquals(request.getLastName(), response.getLastName());
        Assert.assertEquals(request.getPatronymic(), response.getPatronymic());
        Assert.assertEquals(request.getEmail(), response.getEmail());
        Assert.assertEquals(request.getAddress(), response.getAddress());
        Assert.assertEquals(request.getPhone(), response.getPhone());
        Assert.assertEquals(0, response.getDeposit());

        ClientRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(response, getActualUserResponse);
    }

    @Test
    public void testClientRegistrationWithLoginDuplicate() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        userService.registerClient(request, token);

        OnlineShopException e=null;
        try {
            userService.registerClient(request, token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_LOGIN_DUPLICATE, e.getErrorCode());
    }

    @Test
    public void testLoginUser() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        ClientRegistrationResponse response = userService.registerClient(request, token);
        ClientRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(response,getActualUserResponse);

        LoginUserRequest loginRequest = new LoginUserRequest(
                "petrov",
                "123456789");
        String token2 = tokenGenerator.generateToken();

        ClientRegistrationResponse loginResponse = userService.loginUser(loginRequest,token2);
        Assert.assertEquals(response, loginResponse);

        OnlineShopException e=null;
        try {
            userService.getActualUser(token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_OLD_SESSION, e.getErrorCode());

        ClientRegistrationResponse getActualUserResponse2 =userService.getActualUser(token2);
        Assert.assertEquals(response, getActualUserResponse2);
    }

    @Test
    public void testLoginUserByWrongLogin() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        ClientRegistrationResponse response = userService.registerClient(request, token);
        ClientRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(response,getActualUserResponse);

        LoginUserRequest loginRequest = new LoginUserRequest(
                "",
                "123456789");
        String token2 = tokenGenerator.generateToken();

        OnlineShopException e=null;
        try {
            userService.loginUser(loginRequest,token2);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_WRONG_LOGIN_OR_PASSWORD, e.getErrorCode());
    }

    @Test
    public void testLoginUserByWrongPassword() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        ClientRegistrationResponse response = userService.registerClient(request, token);
        ClientRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(response,getActualUserResponse);

        LoginUserRequest loginRequest = new LoginUserRequest(
                "petrov",
                "");
        String token2 = tokenGenerator.generateToken();

        OnlineShopException e=null;
        try {
            userService.loginUser(loginRequest,token2);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_WRONG_LOGIN_OR_PASSWORD, e.getErrorCode());
    }

    @Test
    public void testLogoutUser() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        ClientRegistrationResponse response = userService.registerClient(request, token);
        ClientRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(response,getActualUserResponse);

        String logoutResponse = userService.logoutUser(token);
        Assert.assertEquals("{}", logoutResponse);

        OnlineShopException e=null;
        try {
            userService.getActualUser(token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_OLD_SESSION, e.getErrorCode());
    }


    @Test
    public void testLogoutNonexistentUser() {
        String token = tokenGenerator.generateToken();
        String logoutResponse = userService.logoutUser(token);
        Assert.assertEquals("{}", logoutResponse);

        OnlineShopException e=null;
        try {
            userService.getActualUser(token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_OLD_SESSION, e.getErrorCode());
    }



    @Test
    public void testAllClientsGetting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest request = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                "ivanov",
                "123456789");
        userService.registerAdmin(request, token);

        String token2 = tokenGenerator.generateToken();
        ClientRegistrationRequest request2 = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        ClientRegistrationResponse response2 = userService.registerClient(request2, token2);

        String token3 = tokenGenerator.generateToken();
        ClientRegistrationRequest request3 = new ClientRegistrationRequest(
                "Сергей",
                "Сергеев",
                "Сергеевич",
                "Sergeev@mail.ru",
                "г. Омск, ул. Леконта, д. 1",
                "89082222222",
                "sergeev",
                "1234567890");
        ClientRegistrationResponse response3 = userService.registerClient(request3, token3);

        List<GetAllUsersResponse> allClients = userService.getAllClients(token);
        Assert.assertEquals(2, allClients.size());

        Assert.assertEquals(allClients.get(0).getId(), response2.getId());
        Assert.assertEquals(allClients.get(0).getFirstName(), response2.getFirstName());
        Assert.assertEquals(allClients.get(0).getLastName(), response2.getLastName());
        Assert.assertEquals(allClients.get(0).getPatronymic(), response2.getPatronymic());
        Assert.assertEquals(allClients.get(0).getEmail(), response2.getEmail());
        Assert.assertEquals(allClients.get(0).getAddress(), response2.getAddress());
        Assert.assertEquals(allClients.get(0).getPhone(), response2.getPhone());

        Assert.assertEquals(allClients.get(1).getId(), response3.getId());
        Assert.assertEquals(allClients.get(1).getFirstName(), response3.getFirstName());
        Assert.assertEquals(allClients.get(1).getLastName(), response3.getLastName());
        Assert.assertEquals(allClients.get(1).getPatronymic(), response3.getPatronymic());
        Assert.assertEquals(allClients.get(1).getEmail(), response3.getEmail());
        Assert.assertEquals(allClients.get(1).getAddress(), response3.getAddress());
        Assert.assertEquals(allClients.get(1).getPhone(), response3.getPhone());

        String token4 = tokenGenerator.generateToken();

        OnlineShopException e1=null;
        try{
            userService.getAllClients(token4);
        } catch (OnlineShopException ex){
            e1=ex;
        }
        Assert.assertNotNull(e1);
        Assert.assertEquals(OnlineShopErrorCode.USER_OLD_SESSION, e1.getErrorCode());

        OnlineShopException e2=null;
        try{
            userService.getAllClients(token2);
        } catch (OnlineShopException ex){
            e2=ex;
        }
        Assert.assertNotNull(e2);
        Assert.assertEquals(OnlineShopErrorCode.USER_NOT_ADMIN, e2.getErrorCode());
    }

    @Test
    public void testAdminProfileEditing() throws OnlineShopException {
        String login = "ivanov";
        String oldPassword = "123456789";
        String newPassword = "1234567890";

        String token = tokenGenerator.generateToken();
        AdminRegistrationRequest request = new AdminRegistrationRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "admin1",
                login,
                oldPassword);
        AdminRegistrationResponse response = userService.registerAdmin(request, token);
        Assert.assertNotNull(response);
        Assert.assertNotEquals(0, response.getId());
        Assert.assertEquals(request.getFirstName(), response.getFirstName());
        Assert.assertEquals(request.getLastName(), response.getLastName());
        Assert.assertEquals(request.getPatronymic(), response.getPatronymic());
        Assert.assertEquals(request.getPosition(), response.getPosition());

        AdminRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(response, getActualUserResponse);

        AdminProfileEditingRequest editingRequest = new AdminProfileEditingRequest(
                "Егор",
                "Егоров",
                "Егорович",
                "admin2",
                oldPassword,
                newPassword);

        AdminRegistrationResponse editingResponse = userService.editAdminProfile(editingRequest,token);
        Assert.assertNotNull(editingResponse);
        Assert.assertEquals(response.getId(), editingResponse.getId());
        Assert.assertEquals(editingRequest.getFirstName(), editingResponse.getFirstName());
        Assert.assertEquals(editingRequest.getLastName(), editingResponse.getLastName());
        Assert.assertEquals(editingRequest.getPatronymic(), editingResponse.getPatronymic());
        Assert.assertEquals(editingRequest.getPosition(), editingResponse.getPosition());

        AdminRegistrationResponse getActualUserResponse2 = userService.getActualUser(token);
        Assert.assertEquals(editingResponse, getActualUserResponse2);

        LoginUserRequest loginRequest = new LoginUserRequest(
                login,
                newPassword);
        String token2 = tokenGenerator.generateToken();

        AdminRegistrationResponse loginResponse = userService.loginUser(loginRequest, token2);
        Assert.assertEquals(editingResponse,loginResponse);

        LoginUserRequest loginRequest2 = new LoginUserRequest(
                login,
                oldPassword);
        OnlineShopException e1=null;
        try{
            userService.loginUser(loginRequest2, token2);
        } catch (OnlineShopException ex){
            e1=ex;
        }
        Assert.assertNotNull(e1);
        Assert.assertEquals(OnlineShopErrorCode.USER_WRONG_LOGIN_OR_PASSWORD, e1.getErrorCode());


        AdminProfileEditingRequest editingRequest2 = new AdminProfileEditingRequest(
                "Егор",
                "Егоров",
                "Егорович",
                "admin2",
                oldPassword,
                newPassword);

        OnlineShopException e2=null;
        try{
            userService.editAdminProfile(editingRequest2,token2);
        } catch (OnlineShopException ex){
            e2=ex;
        }
        Assert.assertNotNull(e2);
        Assert.assertEquals(OnlineShopErrorCode.USER_WRONG_PASSWORD, e2.getErrorCode());

    }

    @Test
    public void testClientProfileEditing() throws OnlineShopException {
        String login = "petrov";
        String oldPassword = "123456789";
        String newPassword = "1234567890";

        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                login,
                oldPassword);
        ClientRegistrationResponse response = userService.registerClient(request, token);
        Assert.assertNotNull(response);
        Assert.assertNotEquals(0, response.getId());
        Assert.assertEquals(request.getFirstName(), response.getFirstName());
        Assert.assertEquals(request.getLastName(), response.getLastName());
        Assert.assertEquals(request.getPatronymic(), response.getPatronymic());
        Assert.assertEquals(request.getEmail(), response.getEmail());
        Assert.assertEquals(request.getAddress(), response.getAddress());
        Assert.assertEquals(request.getPhone(), response.getPhone());
        Assert.assertEquals(0, response.getDeposit());

        ClientRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(response, getActualUserResponse);

        ClientProfileEditingRequest editingRequest = new ClientProfileEditingRequest(
                "Егор",
                "Егоров",
                "Егорович",
                "egorov@mail.ru",
                "г. Омск, пр. Комарова, д. 1",
                "admin2",
                oldPassword,
                newPassword);

        ClientRegistrationResponse editingResponse = userService.editClientProfile(editingRequest,token);
        Assert.assertNotNull(editingResponse);
        Assert.assertEquals(response.getId(), editingResponse.getId());
        Assert.assertEquals(editingRequest.getFirstName(), editingResponse.getFirstName());
        Assert.assertEquals(editingRequest.getLastName(), editingResponse.getLastName());
        Assert.assertEquals(editingRequest.getPatronymic(), editingResponse.getPatronymic());
        Assert.assertEquals(editingRequest.getEmail(), editingResponse.getEmail());
        Assert.assertEquals(editingRequest.getAddress(), editingResponse.getAddress());
        Assert.assertEquals(editingRequest.getPhone(), editingResponse.getPhone());
        Assert.assertEquals(0, editingResponse.getDeposit());

        ClientRegistrationResponse getActualUserResponse2 = userService.getActualUser(token);
        Assert.assertEquals(editingResponse, getActualUserResponse2);

        LoginUserRequest loginRequest = new LoginUserRequest(
                login,
                newPassword);
        String token2 = tokenGenerator.generateToken();

        ClientRegistrationResponse loginResponse = userService.loginUser(loginRequest, token2);
        Assert.assertEquals(editingResponse,loginResponse);

        LoginUserRequest loginRequest2 = new LoginUserRequest(
                login,
                oldPassword);
        OnlineShopException e1=null;
        try{
            userService.loginUser(loginRequest2, token2);
        } catch (OnlineShopException ex){
            e1=ex;
        }
        Assert.assertNotNull(e1);
        Assert.assertEquals(OnlineShopErrorCode.USER_WRONG_LOGIN_OR_PASSWORD, e1.getErrorCode());

        ClientProfileEditingRequest editingRequest2 = new ClientProfileEditingRequest(
                "Егор",
                "Егоров",
                "Егорович",
                "egorov@mail.ru",
                "г. Омск, пр. Комарова, д. 1",
                "admin2",
                oldPassword,
                newPassword);

        OnlineShopException e2=null;
        try{
            userService.editClientProfile(editingRequest2,token2);
        } catch (OnlineShopException ex){
            e2=ex;
        }
        Assert.assertNotNull(e2);
        Assert.assertEquals(OnlineShopErrorCode.USER_WRONG_PASSWORD, e2.getErrorCode());
    }

    @Test
    public void testClientDepositMoney() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        ClientRegistrationResponse response = userService.registerClient(request, token);
        Assert.assertEquals(0, response.getDeposit());

        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest("1000");
        ClientRegistrationResponse depositResponse = userService.depositMoney(depositMoneyRequest,token);
        Assert.assertEquals(1000, depositResponse.getDeposit());

        ClientRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(1000, getActualUserResponse.getDeposit());


        DepositMoneyRequest depositMoneyRequest2 = new DepositMoneyRequest("одна тысяча");
        OnlineShopException e = null;
        try {
            userService.depositMoney(depositMoneyRequest2, token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.DEPOSIT_INCORRECT_VALUE, e.getErrorCode());

        ClientRegistrationResponse getActualUserResponse2 = userService.getActualUser(token);
        Assert.assertEquals(1000, getActualUserResponse2.getDeposit());

    }


    @Test
    public void testClientBalanceGetting() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "Петр",
                "Петров",
                "Петрович",
                "petrov@mail.ru",
                "г. Омск, пр. Комарова, д. 2",
                "89081111111",
                "petrov",
                "123456789");
        ClientRegistrationResponse response = userService.registerClient(request, token);
        Assert.assertEquals(0, response.getDeposit());

        ClientRegistrationResponse getBalanceResponse = userService.getBalance(token);
        Assert.assertEquals(0, getBalanceResponse.getDeposit());

        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest("1000");
        ClientRegistrationResponse depositResponse = userService.depositMoney(depositMoneyRequest,token);
        Assert.assertEquals(1000, depositResponse.getDeposit());

        ClientRegistrationResponse getActualUserResponse = userService.getActualUser(token);
        Assert.assertEquals(1000, getActualUserResponse.getDeposit());

        ClientRegistrationResponse getBalanceResponse2 = userService.getBalance(token);
        Assert.assertEquals(1000, getBalanceResponse2.getDeposit());


        DepositMoneyRequest depositMoneyRequest2 = new DepositMoneyRequest("одна тысяча");
        OnlineShopException e = null;
        try {
            userService.depositMoney(depositMoneyRequest2, token);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.DEPOSIT_INCORRECT_VALUE, e.getErrorCode());

        ClientRegistrationResponse getActualUserResponse2 = userService.getActualUser(token);
        Assert.assertEquals(1000, getActualUserResponse2.getDeposit());

        ClientRegistrationResponse getBalanceResponse3 = userService.getBalance(token);
        Assert.assertEquals(1000, getBalanceResponse3.getDeposit());

    }



}
