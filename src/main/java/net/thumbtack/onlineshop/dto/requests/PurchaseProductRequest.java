package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.models.Purchase;

public class PurchaseProductRequest {

    private Integer id;
    private String  name;
    private Integer price;
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Purchase createPurchase(){
        Purchase purchase = new Purchase();
        purchase.setProductId(id);
        purchase.setName(name);
        purchase.setPrice(price);
        purchase.setCount(count);
        return purchase;
    }

}
