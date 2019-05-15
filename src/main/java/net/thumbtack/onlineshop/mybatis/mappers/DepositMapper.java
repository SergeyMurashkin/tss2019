package net.thumbtack.onlineshop.mybatis.mappers;

import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import org.apache.ibatis.annotations.*;

public interface DepositMapper {

    @Insert(" INSERT INTO deposits (id) VALUES  (#{client.id})  ")
    void addDeposit(@Param("client") Client client);

    @Select("SELECT * FROM deposits WHERE id = #{id}")
    Deposit getClientDeposit(@Param("id") Integer clientId);

    @Update("UPDATE deposits SET deposit = (deposit + #{money}), version = version + 1 " +
            "WHERE id = #{deposit.id} AND version = #{deposit.version} ")
    int depositMoney(@Param("deposit") Deposit deposit,
                      @Param("money") Integer money);

    @Update("UPDATE deposits SET deposit = (deposit - #{money}), version = (version+1)" +
            " WHERE id = #{deposit.id} AND version = #{deposit.version}")
    int chargeMoney(@Param("deposit") Deposit deposit,
                     @Param("money") Integer money);

}
