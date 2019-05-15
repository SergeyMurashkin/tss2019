package net.thumbtack.onlineshop.testDao;

import net.thumbtack.onlineshop.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestUserDao extends TestBaseDao {

    @Test
    public void testAdminRegistration() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Admin admin = registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                "ivanov",
                "123456789",
                "admin1",
                token);
        Admin adminFromDB = userDao.getAdmin(userDao.getActualUser(token));
        Assert.assertEquals(admin, adminFromDB);
    }

    @Test(expected = OnlineShopException.class)
    public void testAdminRegistrationWithLoginDuplicate() throws OnlineShopException {

        String token = tokenGenerator.generateToken();
        Admin admin = registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                "ivanov",
                "123456789",
                "admin1",
                token);
        Admin adminFromDB = userDao.getAdmin(userDao.getActualUser(token));
        Assert.assertEquals(admin, adminFromDB);

        String token2 = tokenGenerator.generateToken();
        registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                "ivanov",
                "123456789",
                "admin1",
                token2);
    }


    @Test
    public void testClientRegistration() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Client client = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token);
        Client clientFromDB = userDao.getClient(userDao.getActualUser(token));
        Assert.assertEquals(client, clientFromDB);
    }

    @Test(expected = OnlineShopException.class)
    public void testClientRegistrationWithLoginDuplicate() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Client client = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token);
        Client clientFromDB = userDao.getClient(userDao.getActualUser(token));
        Assert.assertEquals(client, clientFromDB);

        String token2 = tokenGenerator.generateToken();
        registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token2);
    }


    @Test
    public void testClientLogoutAndLogin() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Client client = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token);
        Client clientFromDB = userDao.getClient(userDao.getActualUser(token));
        Assert.assertEquals(client, clientFromDB);

        userDao.logoutUser(token);

        OnlineShopException e = null;
        try {
            userDao.getActualUser(token);
        } catch (OnlineShopException ex) {
            e = ex;
        }
        Assert.assertNotNull(e);

        String token2 = tokenGenerator.generateToken();
        userDao.loginUser(clientFromDB, token2);
        Client clientAfterLoginReceivedByToken2 = userDao.getClient(userDao.getActualUser(token2));
        Assert.assertEquals(clientFromDB, clientAfterLoginReceivedByToken2);

        String token3 = tokenGenerator.generateToken();
        userDao.loginUser(clientFromDB, token3);

        OnlineShopException e2 = null;
        try {
            userDao.getActualUser(token2);
        } catch (OnlineShopException ex) {
            e2 = ex;
        }
        Assert.assertNotNull(e2);

        Client clientAfterLoginReceivedByToken3 = userDao.getClient(userDao.getActualUser(token3));
        Assert.assertEquals(clientFromDB, clientAfterLoginReceivedByToken3);
    }


    @Test
    public void testGettingUserByLoginAndPassword() throws OnlineShopException {
        String login = "ivanov";
        String password = "123456789";
        String token = tokenGenerator.generateToken();
        registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                login,
                password,
                "admin1",
                token);
        User userByToken = userDao.getActualUser(token);
        User userByLoginAndPassword = userDao.getUserByLoginAndPassword(login, password);
        Assert.assertEquals(userByToken, userByLoginAndPassword);
    }


    @Test
    public void testGettingUserByWrongLogin() throws OnlineShopException {
        String login = "ivanov";
        String password = "123456789";
        String token = tokenGenerator.generateToken();
        registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                login,
                password,
                "admin1",
                token);
        OnlineShopException e = null;
        try {
            userDao.getUserByLoginAndPassword("", password);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_WRONG_LOGIN_OR_PASSWORD.getErrorText(), e.getMessage());

    }

    @Test
    public void testGettingUserByWrongPassword() throws OnlineShopException {
        String login = "ivanov";
        String password = "123456789";
        String token = tokenGenerator.generateToken();
        registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                login,
                password,
                "admin1",
                token);
        OnlineShopException e = null;
        try {
            userDao.getUserByLoginAndPassword(login, "");
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_WRONG_LOGIN_OR_PASSWORD.getErrorText(), e.getMessage());
    }


    @Test
    public void testGettingActualUser() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                "ivanov",
                "123456789",
                "admin1",
                token);
        User userByToken = userDao.getActualUser(token);
        Assert.assertNotNull(userByToken);
    }

    @Test
    public void testGettingAdmin() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Admin admin = registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                "ivanov",
                "123456789",
                "admin1",
                token);
        Admin adminByToken = userDao.getAdmin(userDao.getActualUser(token));
        Assert.assertEquals(admin, adminByToken);
    }


    @Test
    public void testGettingAdminByWrongUser() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Admin admin = registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                "ivanov",
                "123456789",
                "admin1",
                token);

        OnlineShopException e = null;
        try {
            userDao.getAdmin(new User(0, null, null, null, null, null, null));
        } catch (OnlineShopException ex) {
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText(),e.getMessage());

    }


    @Test
    public void testGettingClient() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Client client = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token);
        Client clientByToken = userDao.getClient(userDao.getActualUser(token));
        Assert.assertEquals(client, clientByToken);
    }

    @Test
    public void testGettingClientByWrongUser() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Client client = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token);
        OnlineShopException e = null;

        try {
            userDao.getClient(new User(0, null, null, null, null, null, null));
        } catch (OnlineShopException ex) {
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText(),e.getMessage());

    }

    @Test
    public void testGettingAllClients() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Admin admin = registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                "ivanov",
                "123456789",
                "admin1",
                token);
        Admin adminByToken = userDao.getAdmin(userDao.getActualUser(token));
        Assert.assertEquals(admin, adminByToken);

        String token2 = tokenGenerator.generateToken();
        Client client2 = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov2",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token2);
        Client clientByToken2 = userDao.getClient(userDao.getActualUser(token2));
        Assert.assertEquals(client2, clientByToken2);

        String token3 = tokenGenerator.generateToken();
        Client client3 = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov3",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token3);
        Client clientByToken3 = userDao.getClient(userDao.getActualUser(token3));
        Assert.assertEquals(client3, clientByToken3);

        List<Client> clients = new ArrayList<>();
        clients.add(client2);
        clients.add(client3);

        List<Client> allClients = userDao.getAllClients();
        Assert.assertEquals(clients, allClients);
    }


    @Test
    public void testEditingAdminProfile() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Admin admin = registerAdmin(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.ADMIN.name(),
                "ivanov",
                "123456789",
                "admin1",
                token);
        Admin adminBefore = userDao.getAdmin(userDao.getActualUser(token));
        Assert.assertEquals(admin, adminBefore);

        Admin editedAdmin = new Admin(
                adminBefore.getId(),
                "Петров",
                "Петр",
                "Петрович",
                UserType.ADMIN.name(),
                adminBefore.getLogin(),
                "1234567890",
                "1admin");

        userDao.editAdminProfile(editedAdmin);
        Admin adminAfter = userDao.getAdmin(userDao.getActualUser(token));
        Assert.assertEquals(editedAdmin, adminAfter);
        Assert.assertNotEquals(adminBefore, adminAfter);
    }

    @Test
    public void testEditingClientProfile() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Client client = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token);
        Client clientFromDB = userDao.getClient(userDao.getActualUser(token));
        Assert.assertEquals(client, clientFromDB);

        Client newClient = new Client(
                clientFromDB.getId(),
                "Петров",
                "Петр",
                "Петрович",
                UserType.CLIENT.name(),
                clientFromDB.getLogin(),
                "1234567890",
                "petrov@mail.ru",
                "Novosibirsk",
                "89081111111",
                clientFromDB.getDeposit());

        userDao.editClientProfile(newClient);
        Client editedClient = userDao.getClient(userDao.getActualUser(token));
        Assert.assertEquals(newClient, editedClient);
        Assert.assertNotEquals(clientFromDB, editedClient);
    }

    @Test
    public void testDepositMoney() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Client client = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token);
        Client clientFromDB = userDao.getClient(userDao.getActualUser(token));
        Assert.assertEquals(client, clientFromDB);

        userDao.depositMoney(clientFromDB.getDeposit(), 1000);
        Client clientWithDeposit = userDao.getClient(userDao.getActualUser(token));

        Assert.assertEquals(clientFromDB.getId(), clientWithDeposit.getDeposit().getId());
        Assert.assertEquals((Integer) 1, clientWithDeposit.getDeposit().getVersion());
        Assert.assertEquals(1000, clientWithDeposit.getDeposit().getDeposit());

    }

    @Test
    public void testDepositMoneyWithChangedVersion() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
        Client client = registerClient(
                "Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0),
                token);
        Client clientFromDB = userDao.getClient(userDao.getActualUser(token));
        Assert.assertEquals(client, clientFromDB);

        clientFromDB.getDeposit().setVersion(100);
        OnlineShopException e = null;
        try {
            userDao.depositMoney(clientFromDB.getDeposit(), 1000);
        } catch (OnlineShopException ex) {
            e = ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.DEPOSIT_STATE_CHANGING.getErrorText(), e.getMessage());

        Client clientWithDeposit = userDao.getClient(userDao.getActualUser(token));

        Assert.assertEquals(clientFromDB.getId(), clientWithDeposit.getDeposit().getId());
        Assert.assertEquals((Integer) 0, clientWithDeposit.getDeposit().getVersion());
        Assert.assertEquals(0, clientWithDeposit.getDeposit().getDeposit());

    }


}
