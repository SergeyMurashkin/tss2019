package net.thumbtack.onlineshop.model;

public class Deposit {

    private int id;
    private int deposit;
    private Integer version;

    public Deposit(){
    }

    public Deposit(int id, int deposit, Integer version){
        this.id = id;
        this.deposit = deposit;
        this.version = version;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
