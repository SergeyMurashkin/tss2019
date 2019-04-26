package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.*;

import java.util.List;

public interface UserDao {

    boolean isLoginExists(String login);

    void registerAdmin(Admin admin);

    void loginUser(String login, String password, String cookieValue);

    void registerClient(Client client);

    boolean isUserPasswordCorrect(String login, String password);

    User getActualUser(String cookieValue);

    Admin getAdmin(User user);

    Client getClient(User user);

    void logoutUser(String cookieValue);

    List<Client> getAllClients();

    void editAdminProfile(Admin newAdmin);

    void editClientProfile(Client client);

    void depositMoney(Deposit deposit, Integer money) throws OnlineShopException;

}
