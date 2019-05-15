package net.thumbtack.onlineshop.testDao;

import net.thumbtack.onlineshop.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestPurchaseDao extends TestBaseDao {

    @Test
    public void testProductPurchase() throws OnlineShopException {

        String token = tokenGenerator.generateToken();
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
                token);
        Client ivanov = userDao.getClient(userDao.getActualUser(token));

        userDao.depositMoney(ivanov.getDeposit(), 100000);
        ivanov = userDao.getClient(ivanov);
        Assert.assertEquals(100000, ivanov.getDeposit().getDeposit());

        Product apple = new Product(0, "Яблоко", 100, 100, null, null);
        productDao.addProduct(apple, null);
        apple = productDao.getProduct(apple.getId());
        Assert.assertEquals(100, apple.getCount());

        Purchase purchase = new Purchase(0, ivanov.getId(), apple.getId(), "Яблоко", 100, 50);

        purchaseDao.purchaseProduct(ivanov, apple, purchase);
        Assert.assertNotEquals(0, purchase.getId());

        ivanov = userDao.getClient(ivanov);
        Assert.assertEquals(95000, ivanov.getDeposit().getDeposit());

        apple = productDao.getProduct(apple.getId());
        Assert.assertEquals(50, apple.getCount());
    }


    @Test
    public void testProductWithChangedProductVersionPurchase() throws OnlineShopException {

        String token = tokenGenerator.generateToken();
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
                token);
        Client ivanov = userDao.getClient(userDao.getActualUser(token));

        userDao.depositMoney(ivanov.getDeposit(), 100000);
        ivanov = userDao.getClient(ivanov);
        Assert.assertEquals(100000, ivanov.getDeposit().getDeposit());

        Product apple = new Product(0, "Яблоко", 100, 100, null, null);
        productDao.addProduct(apple, null);
        apple = productDao.getProduct(apple.getId());
        Assert.assertEquals(100, apple.getCount());

        Purchase purchase = new Purchase(0, ivanov.getId(), apple.getId(), "Яблоко", 100, 50);

        apple.setVersion(100);
        OnlineShopException e = null;
        try {
            purchaseDao.purchaseProduct(ivanov, apple, purchase);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.PRODUCT_STATE_CHANGING.getErrorText(), e.getMessage());

        ivanov = userDao.getClient(ivanov);
        Assert.assertEquals(100000, ivanov.getDeposit().getDeposit());

        apple = productDao.getProduct(apple.getId());
        Assert.assertEquals(100, apple.getCount());


    }


    @Test
    public void testProductWithChangedDepositVersionPurchase() throws OnlineShopException {

        String token = tokenGenerator.generateToken();
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
                token);
        Client ivanov = userDao.getClient(userDao.getActualUser(token));

        userDao.depositMoney(ivanov.getDeposit(), 100000);
        ivanov = userDao.getClient(ivanov);
        Assert.assertEquals(100000, ivanov.getDeposit().getDeposit());

        Product apple = new Product(0, "Яблоко", 100, 100, null, null);
        productDao.addProduct(apple, null);
        apple = productDao.getProduct(apple.getId());
        Assert.assertEquals(100, apple.getCount());

        Purchase purchase = new Purchase(0, ivanov.getId(), apple.getId(), "Яблоко", 100, 50);

        ivanov.getDeposit().setVersion(100);
        OnlineShopException e = null;
        try {
            purchaseDao.purchaseProduct(ivanov, apple, purchase);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals(OnlineShopErrorCode.DEPOSIT_STATE_CHANGING.getErrorText(), e.getMessage());

        ivanov = userDao.getClient(ivanov);
        Assert.assertEquals(100000, ivanov.getDeposit().getDeposit());

        apple = productDao.getProduct(apple.getId());
        Assert.assertEquals(100, apple.getCount());


    }



    @Test
    public void testBasketProductsPurchase() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
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
                token);
        Client ivanov = userDao.getClient(userDao.getActualUser(token));

        userDao.depositMoney(ivanov.getDeposit(), 100000);
        ivanov = userDao.getClient(ivanov);
        Assert.assertEquals(100000, ivanov.getDeposit().getDeposit());

        Product apple = new Product(0, "Яблоко", 100, 90, null, null);
        productDao.addProduct(apple, null);
        apple = productDao.getProduct(apple.getId());

        Product pear = new Product(0, "Груша", 150, 100, null, null);
        productDao.addProduct(pear, null);
        pear = productDao.getProduct(pear.getId());

        Product orange = new Product(0, "Апельсин", 200, 110, null, null);
        productDao.addProduct(orange, null);
        orange = productDao.getProduct(orange.getId());

        Product productToBasket = new Product(apple.getId(), "Яблоко", 100, 50, null, 0);
        basketDao.addProductInBasket(ivanov, productToBasket);

        Product productToBasket2 = new Product(pear.getId(), "Груша", 150, 50, null, 0);
        basketDao.addProductInBasket(ivanov, productToBasket2);

        Product productToBasket3 = new Product(orange.getId(), "Апельсин", 200, 50, null, 0);
        basketDao.addProductInBasket(ivanov, productToBasket3);

        List<Product> products = Arrays.asList(productToBasket, productToBasket2, productToBasket3);

        purchaseDao.purchaseProductsFromBasket(ivanov, products);

        ivanov = userDao.getClient(ivanov);
        int totalCost =
                productToBasket.getCount() * productToBasket.getPrice() +
                        productToBasket2.getCount() * productToBasket2.getPrice() +
                        productToBasket3.getCount() * productToBasket3.getPrice();
        Assert.assertEquals(100000 - totalCost, ivanov.getDeposit().getDeposit());
        Assert.assertEquals((Integer) 2, ivanov.getDeposit().getVersion());

        apple = productDao.getProduct(apple.getId());
        Assert.assertEquals(40, apple.getCount());
        Assert.assertEquals((Integer)1,apple.getVersion());

        pear = productDao.getProduct(pear.getId());
        Assert.assertEquals(50, pear.getCount());
        Assert.assertEquals((Integer)1,pear.getVersion());

        orange = productDao.getProduct(orange.getId());
        Assert.assertEquals(60, orange.getCount());
        Assert.assertEquals((Integer)1,orange.getVersion());

    }


    @Test
    public void testBasketProductsPurchaseWithChangedProductVersion() throws OnlineShopException {
        String token = tokenGenerator.generateToken();
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
                token);
        Client ivanov = userDao.getClient(userDao.getActualUser(token));

        userDao.depositMoney(ivanov.getDeposit(), 100000);
        ivanov = userDao.getClient(ivanov);
        Assert.assertEquals(100000, ivanov.getDeposit().getDeposit());

        Product apple = new Product(0, "Яблоко", 100, 90, null, null);
        productDao.addProduct(apple, null);
        apple = productDao.getProduct(apple.getId());

        Product pear = new Product(0, "Груша", 150, 100, null, null);
        productDao.addProduct(pear, null);
        pear = productDao.getProduct(pear.getId());

        Product orange = new Product(0, "Апельсин", 200, 110, null, null);
        productDao.addProduct(orange, null);
        orange = productDao.getProduct(orange.getId());

        Product productToBasket = new Product(apple.getId(), "Яблоко", 100, 50, null, 0);
        basketDao.addProductInBasket(ivanov, productToBasket);

        Product productToBasket2 = new Product(pear.getId(), "Груша", 150, 50, null, 1);
        basketDao.addProductInBasket(ivanov, productToBasket2);

        Product productToBasket3 = new Product(orange.getId(), "Апельсин", 200, 50, null, 0);
        basketDao.addProductInBasket(ivanov, productToBasket3);

        List<Product> products = Arrays.asList(productToBasket, productToBasket2, productToBasket3);

        purchaseDao.purchaseProductsFromBasket(ivanov, products);

        ivanov = userDao.getClient(ivanov);
        int totalCost =
                productToBasket.getCount() * productToBasket.getPrice() +
                        productToBasket3.getCount() * productToBasket3.getPrice();
        Assert.assertEquals(100000 - totalCost, ivanov.getDeposit().getDeposit());
        Assert.assertEquals((Integer) 2, ivanov.getDeposit().getVersion());

        apple = productDao.getProduct(apple.getId());
        Assert.assertEquals(40, apple.getCount());
        Assert.assertEquals((Integer)1,apple.getVersion());

        pear = productDao.getProduct(pear.getId());
        Assert.assertEquals(100, pear.getCount());
        Assert.assertEquals((Integer)0,pear.getVersion());

        orange = productDao.getProduct(orange.getId());
        Assert.assertEquals(60, orange.getCount());
        Assert.assertEquals((Integer)1,orange.getVersion());

    }


}
