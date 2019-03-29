package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.models.Admin;

public class AdminProfileEditingRequest {


    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
    private String oldPassword;
    private String newPassword;

    public AdminProfileEditingRequest(){

    }

    public AdminProfileEditingRequest(String firstName,
                               String lastName,
                               String patronymic,
                               String position,
                               String oldPassword,
                               String newPassword){
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Admin createNewUser() {
        Admin admin = new Admin();
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setPatronymic(patronymic);
        admin.setPassword(newPassword);
        admin.setPosition(position);
        return admin;
    }
}
