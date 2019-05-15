package net.thumbtack.onlineshop.mybatis.mappers;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import javax.validation.constraints.Pattern;
import java.util.List;

public interface UserMapper {

    @Select("SELECT IF( (SELECT login FROM users WHERE login = #{login}) IS NULL, false, true)")
    boolean isLoginExists(@Param("login") String login);

    @Insert(" INSERT INTO users (firstName, lastName, patronymic, userType, login, password) VALUES " +
            " ( #{user.firstName}, #{user.lastName}, #{user.patronymic},  " +
            " #{user.userType}, #{user.login}, #{user.password} ) " )
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    void insertUser(@Param("user") User user);

    @Insert(" INSERT INTO admins (id, position) VALUES ( #{admin.id}, #{admin.position} ) " )
    void insertAdmin(@Param("admin") Admin admin);

    @Insert("INSERT INTO sessions (id, cookie) VALUES ( #{user.id}, #{cookie}) " +
            " ON DUPLICATE KEY UPDATE cookie = VALUES(cookie) ")
    void loginUser(@Param("user") User user,
                   @Param("cookie") String cookieValue);

    @Insert(" INSERT INTO clients (id, email, address, phone) VALUES " +
            " ( #{client.id}, #{client.email}, #{client.address}, #{client.phone}) " )
    void insertClient(@Param("client") Client client);

    @Select("SELECT * FROM users WHERE login = #{login} AND password = BINARY #{password} ")
    User getUserByLoginAndPassword(@Param("login") String login,
                                   @Param("password") String password);

    @Select("SELECT * FROM users WHERE id = ( SELECT id FROM sessions WHERE (cookie = #{cookie}))")
    User getActualUser(@Param("cookie") String cookie);

    @Select("SELECT * FROM users INNER JOIN admins USING (id) WHERE id = #{user.id} " )
    Admin getAdmin(@Param("user") User user);

    @Select("SELECT * FROM users INNER JOIN clients USING (id) WHERE id = #{user.id} " )
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "deposit", column = "id", javaType = Deposit.class,
                    one = @One (select = "net.thumbtack.onlineshop.mybatis.mappers.DepositMapper.getClientDeposit", fetchType = FetchType.EAGER))
    })
    Client getClient(@Param("user") User user);

    @Delete(" DELETE FROM sessions WHERE cookie = #{cookieValue}" )
    void logoutUser(String cookieValue);

    @Select("SELECT * FROM users INNER JOIN clients USING (id)" )
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "deposit", column = "id", javaType = Deposit.class,
                    one = @One (select = "net.thumbtack.onlineshop.mybatis.mappers.DepositMapper.getClientDeposit", fetchType = FetchType.EAGER))
    })
    List<Client> getAllClients();

    @Update( " UPDATE users SET firstName = #{user.firstName}, lastName = #{user.lastName},  " +
            " patronymic = #{user.patronymic}, password = #{user.password} WHERE id = #{user.id} ")
    void userProfileEditing(@Param("user") User user);

    @Update( " UPDATE admins SET position = #{admin.position} WHERE id = #{admin.id} ")
    void adminProfileEditing(@Param("admin") Admin admin);

    @Update("UPDATE clients SET email = #{client.email}, address = #{client.address}, phone = #{client.phone} " +
            " WHERE id = #{client.id} ")
    void clientProfileEditing(@Param("client") Client client);

    @Delete("DELETE FROM users" )
    void deleteAllUsers();

    @Select("SELECT id FROM clients")
    List<Integer> getAllClientsId();
}
