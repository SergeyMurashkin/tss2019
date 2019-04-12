package net.thumbtack.onlineshop.model;

public class Client extends User {

    private String email;
    private String address;
    private String phone;
    private Deposit deposit;

    public Client() {
    }

    public Client(int id,
                  String firstName,
                  String lastName,
                  String patronymic,
                  String userType,
                  String login,
                  String password,
                  String email,
                  String address,
                  String phone,
                  Deposit deposit) {
        super(id, firstName, lastName, patronymic, userType, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = deposit;

    }

    public Client(String firstName,
                  String lastName,
                  String patronymic,
                  String userType,
                  String login,
                  String password,
                  String email,
                  String address,
                  String phone,
                  Deposit deposit) {
        super(firstName, lastName, patronymic, userType, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = deposit;
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

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + super.getId() + '\'' +
                ", firstName='" + super.getFirstName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", patronymic='" + super.getPatronymic() + '\'' +
                ", userType='" + super.getUserType() + '\'' +
                ", login='" + super.getLogin() + '\'' +
                ", password='" + super.getPassword() + '\'' +
                ", position='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", deposit='" + deposit + '\'' +
                '}';
    }
}
