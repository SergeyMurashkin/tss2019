package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.models.Category;
import net.thumbtack.onlineshop.models.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.Collection;
import java.util.List;

public interface ProductMapper {
    @Insert("INSERT INTO products (name, price, count) VALUES " +
            "( #{product.name}, #{product.price}, #{product.count})")
    @Options(useGeneratedKeys = true, keyProperty = "product.id")
    void addProduct(@Param("product") Product product);


    @Insert( {"<script>",
            "INSERT INTO products_categories (productId, categoryId) VALUES ",
            "<foreach  item='item' collection='list' open='(' separator='),(' close=')' >",
            "  #{product.id}, #{item.id} ",
            "</foreach>",
            "</script>" })
    void addProductCategories(@Param("list") List<Category> categories,
                              @Param("product") Product product);

    @Update("UPDATE products SET name = #{product.name}, price = #{product.price}," +
            " count = #{product.count} WHERE id = #{product.id}")
    void editProduct(@Param("product") Product product);

    @Delete("DELETE FROM products_categories WHERE productId = #{product.id} ")
    void deleteAllProductCategories( @Param("product") Product product);

    @Delete("DELETE FROM products WHERE id = #{id}")
    void deleteProduct(@Param("id")Integer id);

    @Select("SELECT * FROM products WHERE id = #{id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mybatis.mappers.ProductMapper.getProductCategories", fetchType = FetchType.EAGER))
    })
    Product getProduct(Integer id);

    @Select("SELECT name FROM categories WHERE id IN " +
            "(SELECT categoryId FROM products_categories WHERE productId = #{id}) ORDER BY name ")
    List<Category> getProductCategories(@Param("id") Integer productId);

    @Update("UPDATE products SET count = (count - #{soldCount}) WHERE (id = #{product.id}) ")
    void reduceProductCount(@Param("product") Product product,
                            @Param("soldCount") Integer soldCount);

    @Select("SELECT * FROM products WHERE id NOT IN (SELECT productId FROM products_categories)")
    List<Product> getProductsWithoutCategories();

    @Select("SELECT * FROM products ")
    List<Product> getAllProducts();


    @Select( {"<script>",
            "SELECT * FROM products WHERE id IN ( SELECT productId FROM products_categories WHERE categoryId IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            " ) ORDER BY name ",
            "</script>" })
    List<Product> getProductsWithCategories(@Param("list") List<Integer> categories);
}
