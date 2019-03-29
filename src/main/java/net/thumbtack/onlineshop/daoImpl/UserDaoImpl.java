package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.models.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends DaoImplBase implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void registerAdmin(Admin admin, String cookie) {

        LOGGER.debug("DAO insert Admin with login {}", admin.getLogin());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(admin);
                getUserMapper(sqlSession).insertAdmin(admin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Admin. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            try {
                getUserMapper(sqlSession).loginUser(admin, cookie);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't login User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public void registerClient(Client client, String cookie) {
        LOGGER.debug("DAO insert Client with login {}", client.getLogin());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(client);
                getUserMapper(sqlSession).insertClient(client);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Admin. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            try {
                getUserMapper(sqlSession).loginUser(client, cookie);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't login User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public User loginUser(String login, String password, String cookie) {
        LOGGER.debug("DAO login User with login {}", login);
        User user;
        try (SqlSession sqlSession = getSession()) {
            try {
                user = getUserMapper(sqlSession).getUser(login, password);

                if (user.getUserType().equals(UserType.ADMIN.name())) {
                    user = getUserMapper(sqlSession).getAdmin(user);
                }

                if (user.getUserType().equals(UserType.CLIENT.name())) {
                    user = getUserMapper(sqlSession).getClient(user);
                }

                getUserMapper(sqlSession).loginUser(user, cookie);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't login User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return user;
    }


    @Override
    public void logoutUser(String cookieValue) {
        LOGGER.debug("DAO logout User with cookie {}", cookieValue);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).logoutUser(cookieValue);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't logout User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public User getActualUser(String cookieValue) {
        LOGGER.debug("DAO get User with cookie {}", cookieValue);
        User user;
        try (SqlSession sqlSession = getSession()) {
            try {

                user = getUserMapper(sqlSession).getActualUser(cookieValue);

                if (user.getUserType().equals(UserType.ADMIN.name())) {
                    user = getUserMapper(sqlSession).getAdmin(user);
                }

                if (user.getUserType().equals(UserType.CLIENT.name())) {
                    user = getUserMapper(sqlSession).getClient(user);
                }

            } catch (RuntimeException ex) {
                LOGGER.info("Can't login User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return user;
    }

    @Override
    public List<Client> getAllClients(String cookieValue) {
        LOGGER.debug("DAO get all Users by adminCookie {}", cookieValue);
        List<Client> clients = new ArrayList<>();

        try (SqlSession sqlSession = getSession()) {
            try {
                User user = getUserMapper(sqlSession).getActualUser(cookieValue);

                if (user.getUserType().equals(UserType.ADMIN.name())) {
                    clients = getUserMapper(sqlSession).getAllClients();
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get all Users. {}", ex);
                sqlSession.rollback();
                throw ex;
            }

            sqlSession.commit();
        }
        return clients;
    }

    @Override
    public Admin adminProfileEditing(Admin newAdmin, String cookieValue, String oldPassword) {
        LOGGER.debug("DAO edit Admin profile with cookie {}", cookieValue);
        User user;
        Admin admin;
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).userProfileEditing(newAdmin, cookieValue, oldPassword);
                getUserMapper(sqlSession).adminProfileEditing(newAdmin, cookieValue);
                user = getUserMapper(sqlSession).getActualUser(cookieValue);
                admin = getUserMapper(sqlSession).getAdmin(user);

            } catch (RuntimeException ex) {
                LOGGER.info("Can't edit Admin profile. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return admin;
    }

    @Override
    public Client clientProfileEditing(Client newClient, String cookieValue, String oldPassword) {
        LOGGER.debug("DAO edit Admin profile with cookie {}", cookieValue);
        User user;
        Client client;
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).userProfileEditing(newClient, cookieValue, oldPassword);
                getUserMapper(sqlSession).clientProfileEditing(newClient, cookieValue);
                user = getUserMapper(sqlSession).getActualUser(cookieValue);
                client = getUserMapper(sqlSession).getClient(user);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't edit Admin profile. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return client;
    }

    @Override
    public Client depositMoney(String cookieValue, Integer money) {
        LOGGER.debug("DAO deposit money {}");
        Client client = new Client();
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.CLIENT.name())) {
                try {
                    getUserMapper(sqlSession).depositMoney(user, money);
                    client = getUserMapper(sqlSession).getClient(user);
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't deposit money. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();

            }
        }
        return client;
    }

    @Override
    public Client getMoney(String cookieValue) {
        LOGGER.debug("DAO get money {}");
        Client client = new Client();
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.CLIENT.name())) {
                try {
                    client = getUserMapper(sqlSession).getClient(user);
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't get money. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();

            }
        }
        return client;
    }




}
