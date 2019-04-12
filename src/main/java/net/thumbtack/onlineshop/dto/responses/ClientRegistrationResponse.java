package net.thumbtack.onlineshop.dto.responses;

import net.thumbtack.onlineshop.model.Client;

public class ClientRegistrationResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;
    private int deposit;

    public ClientRegistrationResponse() {
    }

    public ClientRegistrationResponse(int id,
                                      String firstName,
                                      String lastName,
                                      String patronymic,
                                      String email,
                                      String address,
                                      String phone,
                                      int deposit) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = deposit;
    }

    public ClientRegistrationResponse(Client client) {
        id = client.getId();
        firstName = client.getFirstName();
        lastName = client.getLastName();
        patronymic = client.getPatronymic();
        email = client.getEmail();
        address = client.getAddress();
        phone = client.getPhone();
        deposit = client.getDeposit().getDeposit();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}
