package net.thumbtack.onlineshop;

import net.thumbtack.onlineshop.mybatis.utils.MyBatisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class OnlineShopServer {

    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final String COOKIE_JAVASESSIONID = "JAVASESSIONID";

    public static void main(String[] args) {

        MyBatisUtils.initSqlSessionFactory();

        SpringApplication.run(OnlineShopServer.class, args);

    }
}
