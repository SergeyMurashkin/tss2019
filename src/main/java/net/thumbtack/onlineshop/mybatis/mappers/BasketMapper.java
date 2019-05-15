package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BasketMapper {

    @Insert("INSERT INTO basketProducts (clientId, productId, count) VALUES " +
            "( #{client.id}, #{product.id}, #{product.count} ) ON DUPLICATE KEY UPDATE count = VALUES(count)")
    void addProductInBasket(@Param("client")Client client,
                            @Param("product") Product productToBasket);

    @Select("SELECT products.id, products.name, products.price, basketProducts.count FROM products, basketProducts WHERE " +
            " products.id = basketProducts.productId AND basketProducts.clientId = #{client.id} ORDER BY products.name"  )
    List<Product> getClientBasket(@Param("client") Client client);

    @Select("SELECT products.id, products.name, products.price, basketProducts.count FROM products, basketProducts WHERE " +
            " products.id = #{productId} AND basketProducts.clientId = #{client.id} AND basketProducts.productId = #{productId}"  )
    Product getBasketProduct(@Param("client") Client client,
                                   @Param("productId") Integer productId);

    @Delete("DELETE FROM basketProducts WHERE clientId = #{clientId} AND productId = #{productId} ")
    int deleteProductFromBasket(@Param("clientId") int clientId,
                                 @Param("productId") int productId);

    @Update("UPDATE basketProducts SET count = #{product.count} WHERE clientId = #{client.id} AND productId = #{product.id}")
    int changeProductQuantity(@Param("client") Client client,
                               @Param("product") Product newBasketProduct);

}
