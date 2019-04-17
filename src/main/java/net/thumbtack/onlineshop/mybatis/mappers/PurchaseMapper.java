package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Purchase;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface PurchaseMapper {

    @Insert("INSERT INTO purchases (clientId, productId, name, price, count) VALUES " +
            " (#{purchase.clientId}, #{purchase.productId}, #{purchase.name}, #{purchase.price}, #{purchase.count} ) ")
    void addPurchase(@Param("purchase") Purchase purchase);

}
