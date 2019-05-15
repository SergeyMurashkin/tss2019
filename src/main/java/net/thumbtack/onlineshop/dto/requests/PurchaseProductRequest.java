package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.OnlineShopServer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PurchaseProductRequest {

    @Min(1)
    private int id;
    @NotBlank
    @Size(max= OnlineShopServer.MAX_NAME_LENGTH)
    private String name;
    @Min(1)
    private int price;
    @Min(1)
    private int count = 1;

    public PurchaseProductRequest(){
    }

    public PurchaseProductRequest(int id, String name, int price, int count){
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public PurchaseProductRequest(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
        this.name = name.trim();
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
