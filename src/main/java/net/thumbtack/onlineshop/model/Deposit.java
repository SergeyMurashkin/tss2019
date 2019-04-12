package net.thumbtack.onlineshop.model;

public class Deposit {

    private int id;
    private int deposit;

    public Deposit(){
    }

    public Deposit(int id, int deposit){
        this.id = id;
        this.deposit = deposit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}
