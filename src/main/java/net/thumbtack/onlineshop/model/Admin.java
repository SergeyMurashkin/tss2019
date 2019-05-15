package net.thumbtack.onlineshop.model;

public class Admin extends User {

    private String position;

    public Admin() {
    }

    public Admin(int id,
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Admin admin = (Admin) o;

        return getPosition() != null ? getPosition().equals(admin.getPosition()) : admin.getPosition() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
        return result;
    }
}
