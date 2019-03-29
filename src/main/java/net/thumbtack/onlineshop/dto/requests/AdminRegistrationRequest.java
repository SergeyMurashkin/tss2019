package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.models.Admin;
import net.thumbtack.onlineshop.models.User;
import net.thumbtack.onlineshop.models.UserType;

public class AdminRegistrationRequest {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
    private String login;
    private String password;

    AdminRegistrationRequest(){
    }

    AdminRegistrationRequest(String firstName,
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public Admin getAdminFromRequest() {
        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setPatronymic(patronymic);
        admin.setUserType(UserType.ADMIN.name());
        admin.setLogin(login);
        admin.setPassword(password);
        admin.setPosition(position);
        return admin;
    }
}
