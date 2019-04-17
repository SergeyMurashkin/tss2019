package net.thumbtack.onlineshop.dto.requests;

import javax.validation.constraints.Min;

public class PurchaseProductRequest {

    private int id;
    private String name;
    private int price;
    @Min(value = 1)
    private int count = 1;

    public PurchaseProductRequest(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
