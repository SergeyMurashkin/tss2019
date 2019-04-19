package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ProductMapper {
    @Insert("INSERT INTO products (name, price, count) VALUES " +
            "( #{product.name}, #{product.price}, #{product.count})")
    @Options(useGeneratedKeys = true, keyProperty = "product.id")
    void addProduct(@Param("product") Product product);


    @Insert( {"<script>",
            "INSERT INTO products_categories (productId, categoryId) VALUES ",
            "<foreach  item='item' collection='list' open='(' separator='),(' close=')' >",
            "  #{product.id}, #{item} ",
            "</foreach>",
            "</script>" })
    void addProductCategories(@Param("list") List<Integer> categoriesId,
                              @Param("product") Product product);

    @Update("UPDATE products SET name = IFNULL(#{product.name}, name), price = IFNULL(#{product.price}, price), " +
            "count = IFNULL(#{product.count}, count) WHERE id = #{product.id}")
    void editProduct(@Param("product") Product product);

    @Delete("DELETE FROM products_categories WHERE productId = #{product.id} ")
    void deleteAllProductCategories( @Param("product") Product product);

    @Update("UPDATE products SET isDeleted = true WHERE id = #{id}")
    void deleteProduct(@Param("id")Integer id);

    @Select("SELECT id,name,price,count FROM products WHERE id = #{id} AND isDeleted = false ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    one = @One(select = "net.thumbtack.onlineshop.mybatis.mappers.ProductMapper.getProductCategories", fetchType = FetchType.EAGER))
    })
    Product getProduct(@Param("id") Integer id);

    @Select("SELECT * FROM categories WHERE id IN " +
            "(SELECT categoryId FROM products_categories WHERE productId = #{id})  ORDER BY name ")
    List<Category> getProductCategories(@Param("id") Integer productId);

    @Update("UPDATE products SET count = (count - #{soldCount}) WHERE (id = #{product.id}) ")
    void reduceProductCount(@Param("product") Product product,
                            @Param("soldCount") Integer soldCount);

    @Select("SELECT * FROM products WHERE id NOT IN (SELECT productId FROM products_categories) " +
            " AND isDeleted = false ORDER BY name")
    List<Product> getProductsWithoutCategories();

    @Select("SELECT * FROM products WHERE isDeleted = false ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    one = @One(select = "net.thumbtack.onlineshop.mybatis.mappers.ProductMapper.getProductCategories", fetchType = FetchType.EAGER))
    })
    List<Product> getAllProductsByProductOrder();


    @Select( {"<script>",
            "SELECT * FROM products WHERE id IN ( SELECT productId FROM products_categories WHERE categoryId IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            " ) AND isDeleted = false ORDER BY name ",
            "</script>" })
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    one = @One(select = "net.thumbtack.onlineshop.mybatis.mappers.ProductMapper.getProductCategories", fetchType = FetchType.EAGER))
    })
    List<Product> getProductsByCategoriesByProductOrder(@Param("list") List<Integer> categoriesId);


    @Select("SELECT * FROM products WHERE id IN ( SELECT productId FROM products_categories WHERE categoryId = #{id}) " +
            " AND isDeleted = false ORDER BY name")
    List<Product> getProductsByCategory(@Param("id") Integer categoryId);
}
