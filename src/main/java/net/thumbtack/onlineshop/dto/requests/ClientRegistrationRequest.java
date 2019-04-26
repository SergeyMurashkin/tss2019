package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.OnlineShopServer;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import net.thumbtack.onlineshop.model.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ClientRegistrationRequest {

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
    @Email
    private String email;

    @NotBlank
    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    private String address;

    @Pattern(regexp = "(8|\\+7)(-?9)(-?[0-9]){9}")
    private String phone;

    @NotBlank
    @Pattern(regexp = "[A-Za-zА-Яа-я0-9]*")
    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    private String login;

    @NotBlank
    @Size(min=OnlineShopServer.MIN_PASSWORD_LENGTH,
            max=OnlineShopServer.MAX_NAME_LENGTH)
    private String password;

    public ClientRegistrationRequest(){

    }

    public ClientRegistrationRequest(String firstName,
                              String lastName,
                              String patronymic,
                              String email,
                              String address,
                              String phone,
                              String login,
                              String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone.replace("-", "");
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
