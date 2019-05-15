package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface CategoryMapper {

    @Select(" SELECT * FROM categories WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "parentId", column = "parentId"),
            @Result(property = "parentName", column = "parentId", javaType = String.class,
                    one = @One(select = "net.thumbtack.onlineshop.mybatis.mappers.CategoryMapper.getParentName", fetchType = FetchType.EAGER)),
            @Result(property = "childCategories", column = "id", javaType = List.class,
                    one = @One(select = "net.thumbtack.onlineshop.mybatis.mappers.CategoryMapper.getChildCategories", fetchType = FetchType.EAGER))
    })
    Category getCategory(@Param("id") Integer categoryId);

    @Select(" SELECT * FROM categories WHERE parentId = #{id} ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "parentId", column = "parentId"),
            @Result(property = "parentName", column = "parentId", javaType = String.class,
                    one = @One(select = "net.thumbtack.onlineshop.mybatis.mappers.CategoryMapper.getParentName", fetchType = FetchType.EAGER))
    })
    List<Category> getChildCategories(@Param("id") Integer categoryId);

    @Select(" SELECT name FROM categories WHERE id = #{parentId}")
    String getParentName(@Param("parentId") Integer parentId);

    @Insert(" INSERT INTO categories (name, parentId) " +
            " VALUES ( #{category.name}, #{category.parentId} ) " )
    @Options(useGeneratedKeys = true, keyProperty = "category.id")
    void addCategory(@Param("category") Category category) throws PersistenceException;

    @Update(" UPDATE categories SET name = #{category.name}, parentId = #{category.parentId}" +
            "  WHERE id = #{category.id} " )
    void editCategory(@Param("category") Category category);

    @Delete(" DELETE FROM categories WHERE id = #{id}")
    void deleteCategory(@Param("id") Integer categoryId);

    @Select(" SELECT * FROM categories WHERE parentId IS NULL ORDER BY name ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "childCategories", column = "id", javaType = List.class,
                    one = @One(select = "net.thumbtack.onlineshop.mybatis.mappers.CategoryMapper.getChildCategories", fetchType = FetchType.EAGER))
    })
    List<Category> getAllCategories();

    @Select({"<script>",
            "SELECT id FROM categories WHERE id IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            "</script>" })
    List<Integer> getRealCategoriesId(@Param("list") List<Integer> categoriesId);

    @Select({"<script>",
            "SELECT id,name FROM categories WHERE id IN ",
            "<foreach  item='item' collection='list' open='(' separator=',' close=')' >",
            " #{item} ",
            "</foreach>",
            " ORDER BY name",
            "</script>" })
    List<Category> getCategories(@Param("list") List<Integer> categoriesId);

    @Delete("DELETE FROM categories")
    void deleteAllCategories();

    @Select(" SELECT id,name FROM categories ORDER BY name ")
    List<Category> getAllCategoriesAndSubCategoriesSortedByName();

    @Select(" SELECT id FROM categories ")
    List<Integer> getAllCategoriesId();
}
