package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.*;

import java.util.List;

public interface UserDao {

    void registerAdmin(Admin admin, String cookieValue) throws OnlineShopException;

    void loginUser(User user, String cookieValue);

    void registerClient(Client client, String cookieValue) throws OnlineShopException;

    User getUserByLoginAndPassword(String login, String password) throws OnlineShopException;

    User getActualUser(String cookieValue) throws OnlineShopException;

    Admin getAdmin(User user) throws OnlineShopException;

    Client getClient(User user) throws OnlineShopException;

    void logoutUser(String cookieValue);

    List<Client> getAllClients();

    void editAdminProfile(Admin newAdmin);

    void editClientProfile(Client client);

    void depositMoney(Deposit deposit, Integer money) throws OnlineShopException;

    List<Integer> getAllClientsId();
}
