package net.thumbtack.onlineshop.dto.responses;

import net.thumbtack.onlineshop.model.Product;

import java.util.List;

public class PurchaseProductFromBasketResponse {

    private List<Product> bought;
    private List<Product> remaining;

    public PurchaseProductFromBasketResponse(List<Product> bought, List<Product> remaining){
        this.bought = bought;
        this.remaining = remaining;
    }

    public List<Product> getBought() {
        return bought;
    }

    public void setBought(List<Product> bought) {
        this.bought = bought;
    }

    public List<Product> getRemaining() {
        return remaining;
    }

    public void setRemaining(List<Product> remaining) {
        this.remaining = remaining;
    }
}
