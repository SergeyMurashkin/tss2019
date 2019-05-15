package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.dao.CommonDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonDaoImpl extends DaoImplBase implements CommonDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Override
    public void clearDataBase() {
        LOGGER.debug("DAO clear DataBase.");
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).deleteAllUsers();
                getCategoryMapper(sqlSession).deleteAllCategories();
                getProductMapper(sqlSession).deleteAllProducts();
                getPurchaseMapper(sqlSession).deleteAllPurchases();
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear DataBase. {)", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }

    }



}
