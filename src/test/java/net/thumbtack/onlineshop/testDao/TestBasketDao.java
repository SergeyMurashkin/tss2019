package net.thumbtack.onlineshop.testDao;

import net.thumbtack.onlineshop.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestBasketDao extends TestBaseDao {

    @Test
    public void testBasketProductAdding() throws OnlineShopException {

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

        Product apple = new Product(0, "Яблоко", 100, 100, null, null);
        productDao.addProduct(apple,null);
        apple = productDao.getProduct(apple.getId());

        Product productToBasket = new Product(apple.getId(), "Яблоко", 100, 1 ,null,null);
        basketDao.addProductInBasket(ivanov,productToBasket);
        Product productFromBasket = basketDao.getBasketProduct(ivanov,productToBasket.getId());
        Assert.assertEquals(productToBasket,productFromBasket);

        Product productToBasket2 = new Product(apple.getId(), "Яблоко", 100, 1000 ,null,null);
        basketDao.addProductInBasket(ivanov,productToBasket2);
        Product productFromBasket2 = basketDao.getBasketProduct(ivanov,productToBasket2.getId());
        Assert.assertEquals(productToBasket2,productFromBasket2);
    }

    @Test
    public void testClientBasketGetting() throws OnlineShopException {
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

        Product apple = new Product(0, "Яблоко", 100, 90, null, null);
        productDao.addProduct(apple,null);
        apple = productDao.getProduct(apple.getId());

        Product pear = new Product(0, "Груша", 150, 100, null, null);
        productDao.addProduct(pear,null);
        pear = productDao.getProduct(pear.getId());

        Product orange = new Product(0, "Апельсин", 200, 110, null, null);
        productDao.addProduct(orange,null);
        orange = productDao.getProduct(orange.getId());

        Product productToBasket = new Product(apple.getId(), "Яблоко", 100, 100 ,null,null);
        basketDao.addProductInBasket(ivanov,productToBasket);
        Product productFromBasket = basketDao.getBasketProduct(ivanov,productToBasket.getId());

        Product productToBasket2 = new Product(pear.getId(), "Груша", 150, 100 ,null,null);
        basketDao.addProductInBasket(ivanov,productToBasket2);
        Product productFromBasket2 = basketDao.getBasketProduct(ivanov,productToBasket2.getId());

        Product productToBasket3 = new Product(orange.getId(), "Апельсин", 200, 100 ,null,null);
        basketDao.addProductInBasket(ivanov,productToBasket3);
        Product productFromBasket3 = basketDao.getBasketProduct(ivanov,productToBasket3.getId());

        List<Product> clientBasketSample = Arrays.asList(productFromBasket3,productFromBasket2,productFromBasket);
        List<Product> clientBasket = basketDao.getClientBasket(ivanov);
        Assert.assertEquals(clientBasketSample, clientBasket);
    }

    @Test
    public void testNonexistentClientBasketGetting() {

        Client ivanov = new Client(0,"Иванов",
                "Иван",
                "Иванович",
                UserType.CLIENT.name(),
                "ivanov",
                "123456789",
                "ivanov@mail.ru",
                "Omsk",
                "89089999999",
                new Deposit(0, 0, 0));

        List<Product> nonexistentClientBasket = basketDao.getClientBasket(ivanov);
        Assert.assertEquals(new ArrayList<>(), nonexistentClientBasket);
    }


    @Test
    public void testNonexistentBasketProductGetting() throws OnlineShopException {

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

        OnlineShopException e = null;
        try {
            basketDao.getBasketProduct(ivanov, 0);
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("id", e.getField());
    }


    @Test
    public void testBasketProductDeleting() throws OnlineShopException {

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


        Product apple = new Product(0, "Яблоко", 100, 100, null, null);
        productDao.addProduct(apple,null);
        apple = productDao.getProduct(apple.getId());

        Product productToBasket = new Product(apple.getId(), "Яблоко", 100, 1 ,null,null);
        basketDao.addProductInBasket(ivanov,productToBasket);

        Product productFromBasket = basketDao.getBasketProduct(ivanov,productToBasket.getId());
        Assert.assertEquals(productToBasket,productFromBasket);

        basketDao.deleteProductFromBasket(ivanov,productFromBasket.getId());

        OnlineShopException e = null;
        try {
            basketDao.getBasketProduct(ivanov, productFromBasket.getId());
        } catch (OnlineShopException ex){
            e=ex;
        }
        Assert.assertNotNull(e);
        Assert.assertEquals("id", e.getField());
    }

    @Test
    public void testBasketProductQuantityEditing() throws OnlineShopException {

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

        Product apple = new Product(0, "Яблоко", 100, 100, null, null);
        productDao.addProduct(apple,null);
        apple = productDao.getProduct(apple.getId());

        Product productToBasket = new Product(apple.getId(), "Яблоко", 100, 1 ,null,null);
        basketDao.addProductInBasket(ivanov,productToBasket);
        Product productFromBasket = basketDao.getBasketProduct(ivanov,productToBasket.getId());
        Assert.assertEquals(productToBasket,productFromBasket);

        Product productToBasket2 = new Product(apple.getId(), "Яблоко", 100, 1000 ,null,null);
        basketDao.changeBasketProductQuantity(ivanov,productToBasket2);
        Product productFromBasket2 = basketDao.getBasketProduct(ivanov,productToBasket2.getId());
        Assert.assertEquals(productToBasket2,productFromBasket2);
    }


}
