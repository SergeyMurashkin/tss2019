package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl extends DaoImplBase implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void registerAdmin(Admin admin, String cookieValue) throws OnlineShopException {
        LOGGER.debug("DAO register Admin. {}", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(admin);
                getUserMapper(sqlSession).insertAdmin(admin);
                getUserMapper(sqlSession).loginUser(admin, cookieValue);
            } catch (PersistenceException ex) {
                LOGGER.error("Can't register Admin. {}", ex);
                sqlSession.rollback();
                if (ex.getCause().getMessage().contains("Duplicate entry"))
                    throw new OnlineShopException(
                            OnlineShopErrorCode.USER_LOGIN_DUPLICATE,
                            "login",
                            OnlineShopErrorCode.USER_LOGIN_DUPLICATE.getErrorText());
            } catch (RuntimeException ex) {
                LOGGER.error("Can't register Admin. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void loginUser(User user, String cookieValue) {
        LOGGER.debug("DAO login User: {}", user);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).loginUser(user, cookieValue);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't login User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public void registerClient(Client client, String cookieValue) throws OnlineShopException {
        LOGGER.debug("DAO insert Client with login {}", client.getLogin());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(client);
                getUserMapper(sqlSession).insertClient(client);
                client.getDeposit().setId(client.getId());
                getDepositMapper(sqlSession).addDeposit(client);
                getUserMapper(sqlSession).loginUser(client, cookieValue);
            } catch (PersistenceException ex) {
                LOGGER.error("Can't insert Client. {}", ex);
                sqlSession.rollback();
                if (ex.getCause().getMessage().contains("Duplicate entry"))
                    throw new OnlineShopException(
                            OnlineShopErrorCode.USER_LOGIN_DUPLICATE,
                            "login",
                            OnlineShopErrorCode.USER_LOGIN_DUPLICATE.getErrorText());
            } catch (RuntimeException ex) {
                LOGGER.error("Can't insert Client. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) throws OnlineShopException {
        LOGGER.debug("DAO get User by login: {} and password: {}", login, password);
        try (SqlSession sqlSession = getSession()) {
            try {
                User user = getUserMapper(sqlSession).getUserByLoginAndPassword(login, password);
                if (user == null) {
                    throw new OnlineShopException(OnlineShopErrorCode.USER_WRONG_LOGIN_OR_PASSWORD,
                            "Login or Password",
                            OnlineShopErrorCode.USER_WRONG_LOGIN_OR_PASSWORD.getErrorText());
                }
                return user;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't check password. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public User getActualUser(String cookieValue) throws OnlineShopException {
        LOGGER.debug("DAO get User with cookie {}", cookieValue);
        try (SqlSession sqlSession = getSession()) {
            try {
                User user = getUserMapper(sqlSession).getActualUser(cookieValue);
                if (user == null) {
                    throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                            null,
                            OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
                }
                return user;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Admin getAdmin(User user) throws OnlineShopException {
        LOGGER.debug("DAO get Admin with id {}", user.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                Admin admin = getUserMapper(sqlSession).getAdmin(user);
                if (admin == null) {
                    throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                            null,
                            OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
                }
                return admin;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }


    @Override
    public Client getClient(User user) throws OnlineShopException {
        LOGGER.debug("DAO get Client with id {}", user.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                Client client = getUserMapper(sqlSession).getClient(user);
                if (client == null) {
                    throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_CLIENT,
                            null,
                            OnlineShopErrorCode.USER_NOT_CLIENT.getErrorText());
                }
                return client;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void logoutUser(String cookieValue) {
        LOGGER.debug("DAO logout User with cookie {}", cookieValue);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).logoutUser(cookieValue);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't logout User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Client> getAllClients() {
        LOGGER.debug("DAO get all Users {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getAllClients();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get all Users. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void editAdminProfile(Admin newAdmin) {
        LOGGER.debug("DAO edit Admin profile with id: {}", newAdmin.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).userProfileEditing(newAdmin);
                getUserMapper(sqlSession).adminProfileEditing(newAdmin);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't edit Admin profile. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void editClientProfile(Client newClient) {
        LOGGER.debug("DAO edit Client profile with id: {}", newClient.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).userProfileEditing(newClient);
                getUserMapper(sqlSession).clientProfileEditing(newClient);
            } catch (RuntimeException ex) {
                LOGGER.error("Can't edit Admin profile. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void depositMoney(Deposit deposit, Integer money) throws OnlineShopException {
        LOGGER.debug("DAO deposit money {}");
        try (SqlSession sqlSession = getSession()) {
            try {
                if (getDepositMapper(sqlSession).depositMoney(deposit, money) != 1) {
                    LOGGER.info("Can't deposit money.");
                    sqlSession.rollback();
                    throw new OnlineShopException(OnlineShopErrorCode.DEPOSIT_STATE_CHANGING,
                            null,
                            OnlineShopErrorCode.DEPOSIT_STATE_CHANGING.getErrorText());
                }
            } catch (RuntimeException ex) {
                LOGGER.error("Can't deposit money. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Integer> getAllClientsId() {
        LOGGER.debug("DAO get all client id.");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getAllClientsId();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }


}
