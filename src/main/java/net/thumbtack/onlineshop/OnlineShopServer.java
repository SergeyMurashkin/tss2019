package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.jdbc.JdbcUtils;
import net.thumbtack.onlineshop.mybatis.utils.MyBatisUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineShopServer {
    public static void main(String[] args) {

        System.out.println(MyBatisUtils.initSqlSessionFactory());

        System.out.println(JdbcUtils.createConnection());

        SpringApplication.run(OnlineShopServer.class, args);

    }
}
