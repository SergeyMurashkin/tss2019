package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.jdbc.JdbcUtils;
import net.thumbtack.onlineshop.mybatis.utils.MyBatisUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineShopServer {

    public static final int REST_HTTP_PORT = 8080;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 6;

    public static void main(String[] args) {

        System.out.println(MyBatisUtils.initSqlSessionFactory());

        System.out.println(JdbcUtils.createConnection());

        SpringApplication.run(OnlineShopServer.class, args);

    }
}
