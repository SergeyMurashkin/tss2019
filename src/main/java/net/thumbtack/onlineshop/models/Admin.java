package net.thumbtack.onlineshop.models;

public class Admin extends User {

    private String position;

    public Admin() {
    }

    public Admin(Integer id,
                 String firstName,
                 String lastName,
                 String patronymic,
                 String userType,
                 String login,
                 String password,
                 String position) {
        super(id, firstName, lastName, patronymic, userType, login, password);
        this.position = position;
    }

    public Admin(String firstName,
                 String lastName,
                 String patronymic,
                 String userType,
                 String login,
                 String password,
                 String position) {
        super(firstName, lastName, patronymic, userType, login, password);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + super.getId() + '\'' +
                ", firstName='" + super.getFirstName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", patronymic='" + super.getPatronymic() + '\'' +
                ", userType='" + super.getUserType() + '\'' +
                ", login='" + super.getLogin() + '\'' +
                ", password='" + super.getPassword() + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
