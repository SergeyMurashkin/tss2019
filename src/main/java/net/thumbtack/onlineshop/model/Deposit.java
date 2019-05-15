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


    @Override
    public String toString() {
        return "Deposit{" +
                "id=" + id +
                ", deposit=" + deposit +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deposit deposit1 = (Deposit) o;

        if (id != deposit1.id) return false;
        if (deposit != deposit1.deposit) return false;
        return version != null ? version.equals(deposit1.version) : deposit1.version == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + deposit;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
