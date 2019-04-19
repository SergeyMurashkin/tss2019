package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.model.Admin;
import net.thumbtack.onlineshop.model.UserType;

import javax.validation.constraints.*;

public class AdminRegistrationRequest {

    @NotBlank
    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    @Pattern(regexp = "[-А-Яа-я0-9 ]*")
    private String firstName;
    @NotBlank
    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    @Pattern(regexp = "[-А-Яа-я0-9 ]*")
    private String lastName;
    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    @Pattern(regexp = "[-А-Яа-я0-9 ]*")
    private String patronymic;
    @NotBlank
    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    private String position;
    @NotBlank
    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    private String login;
    @NotBlank
    @Size(min=OnlineShopServer.MIN_PASSWORD_LENGTH,
            max=OnlineShopServer.MAX_NAME_LENGTH)
    //If spaces in password are not needed, uncomment the line below.
    //@Pattern(regexp = "[^ ]*")
    private String password;

    public AdminRegistrationRequest(){
    }

    public AdminRegistrationRequest(String firstName,
                             String lastName,
                             String patronymic,
                             String position,
                             String login,
                             String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.login = login;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        if(patronymic!=null){
            this.patronymic = patronymic.trim();
        }
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position.trim();
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
