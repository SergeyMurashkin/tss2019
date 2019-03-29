package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.models.*;

import java.util.List;

public interface UserDao {


    void registerAdmin(Admin admin, String cookie);


    void registerClient(Client client, String cookie);

    User loginUser(String login, String password, String cookie);

    void logoutUser(String cookieValue);

    User getActualUser(String cookieValue);

    List<Client> getAllClients(String cookieValue);

    User adminProfileEditing(Admin newAdmin, String cookieValue, String oldPassword);

    User clientProfileEditing(Client client, String cookieValue, String oldPassword);

    Client depositMoney(String cookieValue, Integer money);

    Client getMoney(String cookieValue);


}
