package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.Product;
import net.thumbtack.onlineshop.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BasketMapper {

    @Insert("INSERT INTO basketProducts (clientId, productId, name, price, count) VALUES " +
            "( #{client.id}, #{product.id}, #{product.name}, #{product.price}, #{product.count} )")
    void addProductInBasket(@Param("product") Product basketProduct,
                            @Param("client")User user);

    @Select("SELECT productId AS id, name, price, count FROM basketProducts WHERE clientId = #{client.id} ")
    List<Product> getClientBasket(@Param("client") User user);

    @Select("SELECT productId AS id, name, price, count FROM basketProducts WHERE " +
            " clientId = #{client.id} AND productId = #{product.id} ")
    Product getClientProduct(@Param("client")User user,
                             @Param("product")Product basketProduct);

    @Delete("DELETE FROM basketProducts WHERE clientId = #{clientId} AND productId = #{productId} ")
    void deleteProductFromBasket(@Param("clientId") int clientId,
                                 @Param("productId") int productId);
}
