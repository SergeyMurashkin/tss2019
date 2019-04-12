package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.model.*;

import java.util.List;

public interface UserDao {


    void registerAdmin(Admin admin, String cookie);


    void registerClient(Client client, String cookie);

    User loginUser(String login, String password, String cookie) throws OnlineShopException;

    void logoutUser(String cookieValue);

    User getActualUser(String cookieValue) throws OnlineShopException;

    List<Client> getAllClients(String cookieValue) throws OnlineShopException;

    User adminProfileEditing(Admin newAdmin, String cookieValue, String oldPassword) throws OnlineShopException;

    User clientProfileEditing(Client client, String cookieValue, String oldPassword) throws OnlineShopException;

    Client depositMoney(String cookieValue, Integer money);

    Client getMoney(String cookieValue);


}
