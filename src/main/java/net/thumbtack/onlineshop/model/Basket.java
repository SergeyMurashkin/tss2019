package net.thumbtack.onlineshop.model;

import java.util.List;

public class Basket {

    List<Product> basketProducts;

    public Basket(){
    }

    public Basket(List<Product> basketProducts){
        this.basketProducts = basketProducts;
    }

    public List<Product> getBasketProducts() {
        return basketProducts;
    }

    public void setBasketProducts(List<Product> basketProducts) {
        this.basketProducts = basketProducts;
    }
}
