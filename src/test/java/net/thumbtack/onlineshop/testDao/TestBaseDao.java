package net.thumbtack.onlineshop.testDao;

import net.thumbtack.onlineshop.TokenGenerator;
import net.thumbtack.onlineshop.dao.*;
import net.thumbtack.onlineshop.daoImpl.*;
import net.thumbtack.onlineshop.model.Admin;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.mybatis.utils.MyBatisUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.assertNotEquals;

public class TestBaseDao {

    private static boolean setUpIsDone = false;
    private CommonDao commonDao = new CommonDaoImpl();
    protected UserDao userDao = new UserDaoImpl();
    protected CategoryDao categoryDao = new CategoryDaoImpl();
    protected ProductDao productDao = new ProductDaoImpl();
    protected BasketDao basketDao = new BasketDaoImpl();
    protected PurchaseDao purchaseDao = new PurchaseDaoImpl();
    protected TokenGenerator tokenGenerator = new TokenGenerator();


    @BeforeClass()
    public static void setUp() {
        if (!setUpIsDone) {
            Assume.assumeTrue(MyBatisUtils.initSqlSessionFactory());
            setUpIsDone = true;
        }
    }

    @Before()
    public void clearDatabase() {
        commonDao.clearDataBase();
    }


    protected Admin registerAdmin(String firstName,
                                  String lastName,
                                  String patronymic,
                                  String userType,
                                  String login,
                                  String password,
                                  String position,
                                  String cookieValue) throws OnlineShopException {
        Admin admin = new Admin(firstName, lastName, patronymic, userType, login, password, position);
        userDao.registerAdmin(admin, cookieValue);
        assertNotEquals(0, admin.getId());
        return admin;
    }

    protected Client registerClient(String firstName,
                                    String lastName,
                                    String patronymic,
                                    String userType,
                                    String login,
                                    String password,
                                    String email,
                                    String address,
                                    String phone,
                                    Deposit deposit,
                                    String cookieValue) throws OnlineShopException {
        Client client = new Client(firstName, lastName, patronymic, userType, login, password, email, address, phone, deposit);
        userDao.registerClient(client, cookieValue);
        assertNotEquals(0, client.getId());
        client.getDeposit().setId(client.getId());
        return client;

    }


}
