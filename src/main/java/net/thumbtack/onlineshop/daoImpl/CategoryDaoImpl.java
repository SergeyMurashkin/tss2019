package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.CategoryDao;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl extends DaoImplBase implements CategoryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);

    @Override
    public Category addCategory(String cookieValue, Category category) throws OnlineShopException {
        LOGGER.debug("DAO add Category with name {}", category.getName());
        Category parentCategory;
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    if (category.getParentId()==null || category.getParentId() == 0) {
                        Category checkCategory = getCategoryMapper(sqlSession).getCategoryByName(category.getName());
                        if(checkCategory!=null){
                            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE,
                                    "name",
                                    OnlineShopErrorCode.CATEGORY_NAME_DUPLICATE.getErrorText());
                        }
                        category.setParentId(null);
                        getCategoryMapper(sqlSession).addCategory(category);
                    } else {
                        parentCategory = getCategoryMapper(sqlSession).getCategory(category.getParentId());
                        if(parentCategory==null){
                            throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                                    "parentId",
                                    OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText());
                        }
                        category.setParentName(parentCategory.getName());
                        getCategoryMapper(sqlSession).addCategory(category);
                    }
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't add Category. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();
            } else {
                throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                        null,
                        OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
            }
        }
        return category;
    }

    @Override
    public Category getCategory(String cookieValue, Integer id) throws OnlineShopException {
        LOGGER.debug("DAO get Category with id {}", id);
        Category category;
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    category = getCategoryMapper(sqlSession).getCategory(id);
                    if(category==null){
                        throw new OnlineShopException(OnlineShopErrorCode.CATEGORY_NOT_EXISTS,
                                "number of category in address line",
                                OnlineShopErrorCode.CATEGORY_NOT_EXISTS.getErrorText());
                    }
                } catch (RuntimeException ex) {
                    LOGGER.info("Can't add Category. {}", ex);
                    sqlSession.rollback();
                    throw ex;
                }
                sqlSession.commit();
            } else {
                throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                        null,
                        OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
            }
        }
        return category;
    }





    //I need another way to check for errors...





    @Override
    public Category editCategory(String cookieValue, Category category) throws OnlineShopException {
        LOGGER.debug("DAO edit Category with name {}", category.getName());
        Category parentCategory;
        try (SqlSession sqlSession = getSession()) {
            User user = getUserMapper(sqlSession).getActualUser(cookieValue);
            if (user == null) {
                throw new OnlineShopException(OnlineShopErrorCode.USER_OLD_SESSION,
                        null,
                        OnlineShopErrorCode.USER_OLD_SESSION.getErrorText());
            }
            if (user.getUserType().equals(UserType.ADMIN.name())) {
                try {
                    if(category.getParentId()!=0) {
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
            } else {
                throw new OnlineShopException(OnlineShopErrorCode.USER_NOT_ADMIN,
                        null,
                        OnlineShopErrorCode.USER_NOT_ADMIN.getErrorText());
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
