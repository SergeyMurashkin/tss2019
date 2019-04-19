package net.thumbtack.onlineshop.model;

public class Purchase {

    private int id;
    private int clientId;
    private int productId;
    private String name;
    private int price;
    private int count;

    public Purchase(){
    }

    public Purchase(int id, int clientId, int productId, String name, int price, int count){
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
