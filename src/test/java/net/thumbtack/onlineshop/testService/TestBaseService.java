package net.thumbtack.onlineshop.testService;

import net.thumbtack.onlineshop.TokenGenerator;
import net.thumbtack.onlineshop.mybatis.utils.MyBatisUtils;
import net.thumbtack.onlineshop.service.*;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;

public class TestBaseService {

    private static boolean setUpIsDone = false;
    private CommonService commonService = new CommonService();
    protected UserService userService = new UserService();
    protected CategoryService categoryService = new CategoryService();
    protected ProductService productService = new ProductService();
    protected BasketService basketService = new BasketService();
    protected PurchaseService purchaseService = new PurchaseService();
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
        commonService.clearDataBase();
    }

}
