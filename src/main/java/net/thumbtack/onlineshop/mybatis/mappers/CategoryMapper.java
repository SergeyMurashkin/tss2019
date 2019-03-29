package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.models.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface CategoryMapper {

    @Insert(" INSERT INTO categories (name, parentId, parentName) " +
            " VALUES ( #{category.name}, #{category.parentId}, #{category.parentName} ) " )
    @Options(useGeneratedKeys = true, keyProperty = "category.id")
    void addCategory(@Param("category") Category category);


    @Select(" SELECT * FROM categories WHERE id = #{id}")
    Category getCategory(@Param("id") Integer id);

    @Update(" UPDATE categories SET name = #{category.name}, parentId = #{category.parentId}," +
            " parentName = #{category.parentName} WHERE id = #{category.id} " )
    void editCategory(@Param("category") Category category);

    @Delete(" DELETE FROM categories WHERE id = #{id}")
    void deleteCategory(@Param("id") Integer id);

    @Select(" SELECT * FROM categories WHERE parentId IS NULL ORDER BY name ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "childCategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mybatis.mappers.CategoryMapper.getChildCategories", fetchType = FetchType.EAGER))
    })
    List<Category> getAllCategories();

    @Select(" SELECT * FROM categories WHERE parentId = #{id} ORDER BY name")
    List<Category> getChildCategories(@Param("id") Integer id);

}
