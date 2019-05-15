package net.thumbtack.onlineshop.dto.requests;

public class DepositMoneyRequest {

    String deposit;

    public DepositMoneyRequest() {

    }

    public DepositMoneyRequest(String deposit) {
        this.deposit=deposit;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }
}
