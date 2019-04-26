package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.OnlineShopServer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginUserRequest {

    @NotBlank
    @Pattern(regexp = "[A-Za-zА-Яа-я0-9]*")
    @Size(max= OnlineShopServer.MAX_NAME_LENGTH)
    private String login;

    @NotBlank
    @Size(min=OnlineShopServer.MIN_PASSWORD_LENGTH,
            max=OnlineShopServer.MAX_NAME_LENGTH)
    private String password;

    public LoginUserRequest(){

    }

    public LoginUserRequest(String login, String password){
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }
}
