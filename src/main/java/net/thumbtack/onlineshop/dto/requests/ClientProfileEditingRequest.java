package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.models.Client;

public class ClientProfileEditingRequest {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;
    private String oldPassword;
    private String newPassword;

    public ClientProfileEditingRequest(){

    }

    public ClientProfileEditingRequest(String firstName,
                                String lastName,
                                String patronymic,
                                String email,
                                String address,
                                String phone,
                                String oldPassword,
                                String newPassword){
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
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

    public Client createNewClient() {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setPatronymic(patronymic);
        client.setPassword(newPassword);
        client.setEmail(email);
        client.setAddress(address);
        client.setPhone(phone);
        return client;
    }
}
