package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CategoryDaoImpl extends DaoImplBase implements CategoryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);

    @Override
    public Category getCategory(Integer id) throws OnlineShopException {
        LOGGER.debug("DAO get Category with id {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Category category = getCategoryMapper(sqlSession).getCategory(id);
                if (category == null) {
                    throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                            "number of category in address line",
                            OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText());
                }
                if (category.getParentId() != null && category.getParentName()!=null) {
                    category.setChildCategories(null);
                }
                return category;
            } catch (RuntimeException ex) {
                LOGGER.error("Can't add Category. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void addCategory(Category category) throws OnlineShopException {
        LOGGER.debug("DAO add Category.{}", category);
        try (SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).addCategory(category);
            } catch (PersistenceException ex) {
                LOGGER.error("Can't add Category. {}", ex);
                sqlSession.rollback();
                if (ex.getCause().getMessage().contains("Duplicate entry"))
                    throw new OnlineShopException(
                            OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE,
                            "name",
                            OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE.getErrorText());
                if (ex.getCause().getMessage().contains("foreign key constraint fails"))
                    throw new OnlineShopException(
                            OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                            "parentId",
                            OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText());
            } catch (RuntimeException ex) {
                LOGGER.error("Can't add Category. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void editCategory(Category category) throws OnlineShopException {
        LOGGER.debug("DAO edit Category. {}", category);
        try (SqlSession sqlSession = getSession()) {
            try {
                try {
                    getCategoryMapper(sqlSession).editCategory(category);
                } catch (PersistenceException ex) {
                    LOGGER.error("Can't edit Category. {}", ex);
                    sqlSession.rollback();
                    if (ex.getCause().getMessage().contains("Duplicate entry"))
                        throw new OnlineShopException(
                                OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE,
                                "name",
                                OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE.getErrorText());
                    if (ex.getCause().getMessage().contains("foreign key constraint fails"))
                        throw new OnlineShopException(
                                OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                                "parentId",
                                OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText());
                }
            } catch (RuntimeException ex) {
                LOGGER.error("Can't edit Category. {}", ex);
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
                LOGGER.error("Can't delete Category. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Category> getAllCategories() {
        LOGGER.debug("DAO get all Categories");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).getAllCategories();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get all Categories. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }

    @Override
    public List<Integer> getAllCategoriesAndSubCategoriesId() {
        LOGGER.debug("DAO get all categories Id");
        try (SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).getAllCategoriesId();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't get all categories Id. {}", ex);
                sqlSession.rollback();
                throw ex;
            }
        }
    }


}
