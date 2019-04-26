package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.model.Category;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CategoryDaoImpl extends DaoImplBase implements CategoryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);

    @Override
    public boolean isCategoryNameExists(String name) {
        LOGGER.debug("DAO check Category name: {}", name);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).isCategoryNameExists(name);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Category. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Category getCategory(Integer id) {
        LOGGER.debug("DAO get Category with id {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).getCategory(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Category. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void addCategory(Category category) {
        LOGGER.debug("DAO add Category with name {}", category.getName());
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).addCategory(category);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add Category. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void editCategory(Category category) {
        LOGGER.debug("DAO edit Category with name {}", category.getName());
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).editCategory(category);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't edit Category. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteCategory(Integer id) {
        LOGGER.debug("DAO delete Category with id {}", id);
        try (SqlSession sqlSession = getSession()) {
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

    @Override
    public List<Category> getAllCategories() {
        LOGGER.debug("DAO get all Categories");
        List<Category> categories;
        try (SqlSession sqlSession = getSession()) {
            try {
                categories = getCategoryMapper(sqlSession).getAllCategories();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't get all Categories. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return categories;
    }

    @Override
    public List<Integer> checkCategories(List<Integer> categoriesId) {
        LOGGER.debug("DAO check Categories with id: {}", categoriesId);
        List<Integer> realCategoriesId;
        try (SqlSession sqlSession = getSession()) {
            try {
                realCategoriesId = getCategoryMapper(sqlSession).getCategoriesId(categoriesId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't check Categories. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return realCategoriesId;
    }

}
