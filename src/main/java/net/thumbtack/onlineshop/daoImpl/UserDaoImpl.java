package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.UserDao;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl extends DaoImplBase implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);


    @Override
    public boolean isLoginExists(String login) {
        LOGGER.debug("DAO check login {}", login);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).isLoginExists(login);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't check login. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void registerAdmin(Admin admin) {
        LOGGER.debug("DAO insert Admin with login {}", admin.getLogin());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(admin);
                getUserMapper(sqlSession).insertAdmin(admin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't login User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void loginUser(String login, String password, String cookieValue) {
        LOGGER.debug("DAO login User with login {}", login);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).loginUser(login, password, cookieValue);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't login User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    public void registerClient(Client client) {
        LOGGER.debug("DAO insert Client with login {}", client.getLogin());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(client);
                getUserMapper(sqlSession).insertClient(client);
                client.getDeposit().setId(client.getId());
                getDepositMapper(sqlSession).addDeposit(client);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Admin. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public boolean isUserPasswordCorrect(String login, String password) {
        LOGGER.debug("DAO check password. User with login: {}", login);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).isUserPasswordCorrect(login, password);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't check password. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public User getActualUser(String cookieValue) {
        LOGGER.debug("DAO get User with cookie {}", cookieValue);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getActualUser(cookieValue);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Admin getAdmin(User user) {
        LOGGER.debug("DAO get Admin with id {}", user.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getAdmin(user);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }


    @Override
    public Client getClient(User user) {
        LOGGER.debug("DAO get Client with id {}", user.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getClient(user);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get User. {}", ex);
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
                LOGGER.info("Can't logout User. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Client> getAllClients() {
        LOGGER.debug("DAO get all Users {}");
        List<Client> clients;
        try (SqlSession sqlSession = getSession()) {
            try {
                clients = getUserMapper(sqlSession).getAllClients();
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
    public void editAdminProfile(Admin newAdmin) {
        LOGGER.debug("DAO edit Admin profile with id: {}", newAdmin.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).userProfileEditing(newAdmin);
                getUserMapper(sqlSession).adminProfileEditing(newAdmin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't edit Admin profile. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void editClientProfile(Client newClient){
        LOGGER.debug("DAO edit Client profile with id: {}", newClient.getId());
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).userProfileEditing(newClient);
                getUserMapper(sqlSession).clientProfileEditing(newClient);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't edit Admin profile. {}", ex);
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
                if (getDepositMapper(sqlSession).depositMoney(deposit, money)!=1){
                    throw new OnlineShopException(OnlineShopErrorCode.TRANSACTION_CONFLICT,
                            null,
                            OnlineShopErrorCode.TRANSACTION_CONFLICT.getErrorText());
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't deposit money. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

}
