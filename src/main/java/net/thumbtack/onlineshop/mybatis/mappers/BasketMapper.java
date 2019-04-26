package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Product;
import net.thumbtack.onlineshop.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BasketMapper {

    @Select("SELECT IF( (SELECT count FROM basketProducts WHERE clientId = #{client.id} " +
            "AND productId = #{product.id}) IS NULL, false, true)")
    boolean checkIsProductInClientBasket(@Param("client") Client client,
                                         @Param("product") Product basketProduct);

    @Insert("INSERT INTO basketProducts (clientId, productId, count) VALUES " +
            "( #{client.id}, #{product.id}, #{product.count} )")
    void addProductInBasket(@Param("client")Client client,
                            @Param("product") Product productToBasket);

    @Select("SELECT products.id, products.name, products.price, products.version, basketProducts.count FROM products, basketProducts WHERE " +
            " products.id = basketProducts.productId AND basketProducts.clientId = #{client.id} "  )
    List<Product> getClientBasket(@Param("client") Client client);

    @Select("SELECT products.id, products.name, products.price, basketProducts.count FROM products, basketProducts WHERE " +
            " products.id = #{product.id} AND basketProducts.clientId = #{client.id} "  )
    Product getBasketProduct(@Param("client") Client client,
                                   @Param("product") Product product);

    @Delete("DELETE FROM basketProducts WHERE clientId = #{clientId} AND productId = #{productId} ")
    void deleteProductFromBasket(@Param("clientId") int clientId,
                                 @Param("productId") int productId);

    @Update("UPDATE basketProducts SET count = #{product.count} WHERE clientId = #{client.id} AND productId = #{product.id}")
    void changeProductQuantity(@Param("client") Client client,
                               @Param("product") Product newBasketProduct);

}
