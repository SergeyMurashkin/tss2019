package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.models.Admin;
import net.thumbtack.onlineshop.models.Client;
import net.thumbtack.onlineshop.models.User;
import net.thumbtack.onlineshop.models.UserType;

public class ClientRegistrationRequest {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;
    private String login;
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
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Client getUserFromRequest() {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setPatronymic(patronymic);
        client.setUserType(UserType.CLIENT.name());
        client.setLogin(login);
        client.setPassword(password);
        client.setEmail(email);
        client.setAddress(address);
        client.setPhone(phone);
              return client;
    }
}
