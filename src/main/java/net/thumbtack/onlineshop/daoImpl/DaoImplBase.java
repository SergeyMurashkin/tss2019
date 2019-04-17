package net.thumbtack.onlineshop.daoImpl;

import net.thumbtack.onlineshop.mybatis.mappers.*;
import net.thumbtack.onlineshop.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class DaoImplBase {

    protected SqlSession getSession() {
        return MyBatisUtils.getSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected CategoryMapper getCategoryMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(CategoryMapper.class);
    }

    protected ProductMapper getProductMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ProductMapper.class);
    }

    protected PurchaseMapper getPurchaseMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(PurchaseMapper.class);
    }

    protected DepositMapper getDepositMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(DepositMapper.class);
    }

    protected BasketMapper getBasketMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(BasketMapper.class);
    }


}