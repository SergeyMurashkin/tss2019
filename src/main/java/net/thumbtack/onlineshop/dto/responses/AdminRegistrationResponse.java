package net.thumbtack.onlineshop.dto.responses;

import net.thumbtack.onlineshop.model.Admin;

public class AdminRegistrationResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;

    public AdminRegistrationResponse() {

    }

    public AdminRegistrationResponse(int id,
                                     String firstName,
                                     String lastName,
                                     String patronymic,
                                     String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
    }

    public AdminRegistrationResponse(Admin admin) {
        id = admin.getId();
        firstName = admin.getFirstName();
        lastName = admin.getLastName();
        patronymic = admin.getPatronymic();
        position = admin.getPosition();
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


}
