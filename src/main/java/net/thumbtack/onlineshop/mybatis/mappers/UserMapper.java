package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.models.Admin;
import net.thumbtack.onlineshop.models.Client;
import net.thumbtack.onlineshop.models.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    @Insert(" INSERT INTO users (firstName, lastName, patronymic, userType, login, password) VALUES " +
            " ( #{user.firstName}, #{user.lastName}, #{user.patronymic},  " +
            " #{user.userType}, #{user.login}, #{user.password} ) " )
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    public void insertUser(@Param("user") User user);

    @Insert(" INSERT INTO admins (id, position) VALUES ( #{admin.id}, #{admin.position} ) " )
    public void insertAdmin(@Param("admin") Admin admin);

    @Insert(" INSERT INTO clients (id, email, address, phone) VALUES " +
            " ( #{client.id}, #{client.email}, #{client.address}, #{client.phone}) " )
    public void insertClient(@Param("client") Client client);

    @Insert(" INSERT INTO sessions (id, cookie) VALUES " +
            " ( #{user.id}, #{cookie} ) ")
    public void loginUser(@Param("user") User user,
                          @Param("cookie") String cookie);

    @Select("SELECT * FROM users WHERE (login = #{login} AND password = #{password} ) ")
    User getUser(@Param("login") String login,
                  @Param("password") String password);

    @Select("SELECT * FROM users INNER JOIN admins USING (id) WHERE (login = #{user.login} AND password = #{user.password} ) " )
    Admin getAdmin(@Param("user") User user);

    @Select("SELECT * FROM users INNER JOIN clients USING (id) WHERE (login = #{user.login} AND password = #{user.password} ) " )
    Client getClient(@Param("user") User user);

    @Delete(" DELETE FROM sessions WHERE cookie = #{cookieValue}" )
    void logoutUser(String cookieValue);

    @Select("SELECT * FROM users WHERE id IN ( SELECT id FROM sessions WHERE (cookie = #{cookie}) )  " )
    User getActualUser(@Param("cookie") String cookie);

    @Select("SELECT id, firstName, lastName, patronymic, userType, email, address, phone FROM users INNER JOIN clients USING (id)" )
    List<Client> getAllClients();

    @Update( " UPDATE users SET firstName = #{user.firstName}, lastName = #{user.lastName},  " +
    " patronymic = #{user.patronymic}, password = #{user.password} WHERE id IN " +
    " (SELECT id FROM sessions WHERE cookie = #{cookie}) AND password = #{oldPassword} ")
    void userProfileEditing(@Param("user") User user,
                             @Param("cookie") String cookie,
                             @Param("oldPassword") String oldPassword);

    @Update( " UPDATE admins SET position = #{admin.position} WHERE id IN " +
            " (SELECT id FROM sessions WHERE cookie = #{cookie}) ")
    void adminProfileEditing(@Param("admin") Admin admin,
                            @Param("cookie") String cookie);

    @Update("UPDATE clients SET email = #{client.email}, address = #{client.address}, phone = #{client.phone} " +
            " WHERE id IN (SELECT id FROM sessions WHERE cookie = #{cookie}) ")
    void clientProfileEditing(@Param("client") Client client,
                              @Param("cookie") String cookieValue);

    @Update("UPDATE clients SET deposit = (deposit + #{money}) WHERE id = #{user.id} ")
    void depositMoney(@Param("user")User user,
                      @Param("money")Integer money);

    @Update("UPDATE clients SET deposit = (deposit - #{money}) WHERE id = #{client.id} ")
    void spendMoney(@Param("client") Client client,
                    @Param("money") Integer money);
}
