package net.thumbtack.onlineshop.dto.responses;

import java.util.List;

public class PurchaseProductFromBasketResponse {

    private List<PurchaseProductResponse> bought;
    private List<PurchaseProductResponse> remaining;

    public PurchaseProductFromBasketResponse(List<PurchaseProductResponse> bought, List<PurchaseProductResponse> remaining){
        this.bought = bought;
        this.remaining = remaining;
    }

    public List<PurchaseProductResponse> getBought() {
        return bought;
    }

    public void setBought(List<PurchaseProductResponse> bought) {
        this.bought = bought;
    }

    public List<PurchaseProductResponse> getRemaining() {
        return remaining;
    }

    public void setRemaining(List<PurchaseProductResponse> remaining) {
        this.remaining = remaining;
    }
}
