package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.Product;
import net.thumbtack.onlineshop.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BasketMapper {

    @Insert("INSERT INTO basketProducts (clientId, productId, count) VALUES " +
            "( #{client.id}, #{product.id}, #{product.count} )")
    void addProductInBasket(@Param("product") Product basketProduct,
                            @Param("client")User user);

    @Select("SELECT products.id, products.name, products.price, basketProducts.count FROM products, basketProducts WHERE " +
            " products.id = basketProducts.productId AND basketProducts.clientId = #{client.id} "  )
    List<Product> getClientBasket(@Param("client") User user);

    @Select("SELECT products.id, products.name, products.price, basketProducts.count FROM products, basketProducts WHERE " +
            " products.id = #{product.id} AND basketProducts.clientId = #{client.id} ")
    Product getClientProduct(@Param("client") User user,
                             @Param("product") Product basketProduct);

    @Delete("DELETE FROM basketProducts WHERE clientId = #{clientId} AND productId = #{productId} ")
    void deleteProductFromBasket(@Param("clientId") int clientId,
                                 @Param("productId") int productId);

    @Update("UPDATE basketProducts SET count = #{product.count} WHERE clientId = #{client.id} AND productId = #{product.id}")
    void changeProductQuantity(@Param("client") User user,
                               @Param("product") Product newBasketProduct);
}
