package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.OnlineShopServer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AdminProfileEditingRequest {

    @NotBlank
    @Size(max= OnlineShopServer.MAX_NAME_LENGTH)
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
    @Size(min=OnlineShopServer.MIN_PASSWORD_LENGTH,
            max=OnlineShopServer.MAX_NAME_LENGTH)
    private String oldPassword;

    @NotBlank
    @Size(min=OnlineShopServer.MIN_PASSWORD_LENGTH,
            max=OnlineShopServer.MAX_NAME_LENGTH)
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword.trim();
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword.trim();
    }

}
