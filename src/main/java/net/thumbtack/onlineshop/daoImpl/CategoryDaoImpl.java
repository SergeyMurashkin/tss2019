package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.models.Category;
import net.thumbtack.onlineshop.models.User;
import net.thumbtack.onlineshop.models.UserType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryDaoImpl extends DaoImplBase implements CategoryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);

    @Override
    public Category addCategory(String cookieValue, Category category) {
        LOGGER.debug("DAO add Category with name {}", category.getName());
        Category parentCategory;
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    if (category.getParentId() == null || category.getParentId() == 0 ) {
                        category.setParentId(null);
                        getCategoryMapper(sqlSession).addCategory(category);
                    } else {
                        parentCategory = getCategoryMapper(sqlSession).getCategory(category.getParentId());
                        category.setParentName(parentCategory.getName());
                        getCategoryMapper(sqlSession).addCategory(category);
                    }
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't add Category. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();
            }
        }
        return category;
    }

    @Override
    public Category getCategory(String cookieValue, Integer id) {
        LOGGER.debug("DAO get Category with id {}", id);
        Category category = new Category();
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    category = getCategoryMapper(sqlSession).getCategory(id);
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't add Category. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();
            }
        }
        return category;
    }

    @Override
    public Category editCategory(String cookieValue, Category category) {
        LOGGER.debug("DAO edit Category with name {}", category.getName());
        Category parentCategory;
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    if (category.getParentId()==null || category.getParentId()==0){
                        category.setParentId(null);
                    }else{
                        parentCategory = getCategoryMapper(sqlSession).getCategory(category.getParentId());
                        category.setParentName(parentCategory.getName());
                    }
                    getCategoryMapper(sqlSession).editCategory(category);
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't edit Category. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();
            }
        }
        return category;
    }

    @Override
    public void deleteCategory(String cookieValue, Integer id) {
        LOGGER.debug("DAO delete Category with id {}", id);
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    getCategoryMapper(sqlSession).deleteCategory(id);
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't delete Category. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();
            }
        }
    }

    @Override
    public List<Category> getAllCategories(String cookieValue) {
        LOGGER.debug("DAO get all Categories");
        List<Category> categories = new ArrayList<>();
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    categories = getCategoryMapper(sqlSession).getAllCategories();
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't add Category. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();
            }
        }
        return categories;
    }


}
