package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Purchase;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PurchaseMapper {

    @Insert("INSERT INTO purchases (clientId, productId, name, price, count) VALUES " +
            " (#{purchase.clientId}, #{purchase.productId}, #{purchase.name}, #{purchase.price}, #{purchase.count} ) ")
    @Options(useGeneratedKeys = true, keyProperty = "purchase.id")
    int addPurchase(@Param("purchase") Purchase purchase);

    @Delete("DELETE FROM purchases")
    void deleteAllPurchases();

    @Select("SELECT * FROM purchases WHERE productId NOT IN (SELECT productId FROM products_categories)")
    List<Purchase> getPurchasesByWithoutCategoryNoLimit();

    @Select({"<script>",
            "SELECT * FROM purchases WHERE productId NOT IN (SELECT productId FROM products_categories) ",
            " AND productId IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>"})
    List<Purchase> getPurchasesByWithoutCategoryLimitProducts(@Param("list") List<Integer> productsId);

    @Select({"<script>",
            "SELECT * FROM purchases WHERE productId NOT IN (SELECT productId FROM products_categories) ",
            " AND clientId IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>"})
    List<Purchase> getPurchasesByWithoutCategoryLimitClients(@Param("list") List<Integer> clientsId);

    @Select({"<script>",
            "SELECT * FROM purchases WHERE productId NOT IN (SELECT productId FROM products_categories) ",
            " AND productId IN ",
            "<foreach  item='item' collection='list1' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            " AND clientId IN ",
            "<foreach  item='item' collection='list2' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>"})
    List<Purchase> getPurchasesByWithoutCategoryLimitProductsAndClients(@Param("list1") List<Integer> productsId,
                                                                        @Param("list2") List<Integer> clientsId);

    @Select("SELECT * FROM purchases WHERE productId IN " +
            "(SELECT productId FROM products_categories WHERE categoriesId = #{categoryId})")
    List<Purchase> getPurchasesByCategoryNoLimit(@Param("categoryId") int categoryId);

    @Select({"<script>",
            "SELECT * FROM purchases WHERE productId IN " +
                    "(SELECT productId FROM products_categories WHERE categoriesId = #{categoryId})",
            " AND productId IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>"})
    List<Purchase> getPurchasesByCategoryLimitProducts(@Param("categoryId") int categoryId,
                                                       @Param("list") List<Integer> productsId);


    @Select({"<script>",
            "SELECT * FROM purchases WHERE productId IN " +
                    "(SELECT productId FROM products_categories WHERE categoriesId = #{categoryId})",
            " AND clientId IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>"})
    List<Purchase> getPurchasesByCategoryLimitClients(@Param("categoryId") int categoryId,
                                                      @Param("list") List<Integer> clientsId);

    @Select({"<script>",
            "SELECT * FROM purchases WHERE productId IN " +
                    "(SELECT productId FROM products_categories WHERE categoriesId = #{categoryId})",
            " AND productId IN ",
            "<foreach  item='item' collection='list1' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            " AND clientId IN ",
            "<foreach  item='item' collection='list2' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>"})
    List<Purchase> getPurchasesByCategoryLimitProductsAndClients(@Param("categoryId") int categoryId,
                                                                 @Param("list1") List<Integer> productsId,
                                                                 @Param("list2") List<Integer> clientsId);


    @Select("SELECT * FROM purchases WHERE productId = #{productId} ")
    List<Purchase> getPurchasesByProductNoLimit(@Param("productId") int productId);


    @Select({"<script>",
            "SELECT * FROM purchases WHERE productId = #{productId} AND clientId IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>"})
    List<Purchase> getPurchasesByProductLimitClients(@Param("productId") int productId,
                                                     @Param("list") List<Integer> clientsId);

    @Select("SELECT * FROM purchases WHERE clientId = #{clientId} ")
    List<Purchase> getPurchasesByClientNoLimit(@Param("clientId") int clientId);

    @Select({"<script>",
            "SELECT * FROM purchases WHERE clientId = #{clientId} AND productId IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>"})
    List<Purchase> getPurchasesByClientLimitProducts(@Param("clientId")int clientId,
                                                     @Param("list")List<Integer> productsId);
}
