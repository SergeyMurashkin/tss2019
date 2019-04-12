package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.model.Purchase;

public interface PurchaseDao {

    void purchaseProduct(String cookieValue, Purchase purchase);
}
