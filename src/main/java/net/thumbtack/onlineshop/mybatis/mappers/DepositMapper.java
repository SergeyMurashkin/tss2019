package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface DepositMapper {

    @Update("UPDATE deposits SET deposit = (deposit + #{money}) WHERE id = #{user.id} ")
    void depositMoney(@Param("user") User user,
                      @Param("money")Integer money);

    @Select("SELECT * FROM deposits WHERE id = #{id}")
    Deposit getClientDeposit(@Param("id") Integer clientId);


    @Update("UPDATE deposits SET deposit = (deposit - #{money}) WHERE id = #{client.id} ")
    void spendMoney(@Param("client") Client client,
                    @Param("money") Integer money);

    @Insert(" INSERT INTO deposits (id) VALUES  (#{client.id})  ")
    void addDeposit(@Param("client") Client client);

}
