package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.Collection;
import java.util.List;

public interface CategoryMapper {

    @Insert(" INSERT INTO categories (name, parentId) " +
            " VALUES ( #{category.name}, #{category.parentId} ) " )
    @Options(useGeneratedKeys = true, keyProperty = "category.id")
    void addCategory(@Param("category") Category category);

    @Select(" SELECT * FROM categories WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "childCategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mybatis.mappers.CategoryMapper.getChildCategories", fetchType = FetchType.EAGER))
    })
    Category getCategory(@Param("id") Integer id);

    @Select(" SELECT * FROM categories WHERE name = #{name}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "childCategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mybatis.mappers.CategoryMapper.getChildCategories", fetchType = FetchType.EAGER))
    })
    Category getCategoryByName(@Param("name") String name);

    @Update(" UPDATE categories SET name = #{category.name}, parentId = #{category.parentId}" +
            "  WHERE id = #{category.id} " )
    void editCategory(@Param("category") Category category);

    @Delete(" DELETE FROM categories WHERE id = #{id}")
    void deleteCategory(@Param("id") Integer id);

    @Select(" SELECT * FROM categories WHERE parentId = 0 ORDER BY name ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "childCategories", column = "id", javaType = List.class,
                    one = @One(select = "net.thumbtack.onlineshop.mybatis.mappers.CategoryMapper.getChildCategories", fetchType = FetchType.EAGER))
    })
    List<Category> getAllCategories();

    @Select(" SELECT * FROM categories WHERE parentId = #{id} ORDER BY name")
    List<Category> getChildCategories(@Param("id") Integer id);

    @Select({"<script>",
            "SELECT * FROM categories WHERE id IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>" })

    List<Category> getCategories(@Param("list") List<Integer> categoriesId);
}
