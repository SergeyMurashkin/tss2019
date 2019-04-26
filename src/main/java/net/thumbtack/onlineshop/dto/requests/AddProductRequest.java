package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.OnlineShopServer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class AddProductRequest {

    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    private String name;
    @Min(1)
    private int price;
    @Min(0)
    private int count;
    private List<@Min(1) Integer> categoriesId;


    public AddProductRequest() {
    }

    public AddProductRequest(String name, int price, int count, List<Integer> categoriesId) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.categoriesId = categoriesId;
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

    public List<Integer> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(List<Integer> categoriesId) {
        if (categoriesId==null){
            this.categoriesId = new ArrayList<>();
        } else {
            this.categoriesId = categoriesId;
        }
    }

    @Override
    public String toString() {
        return "AddProductRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", categoriesId=" + categoriesId +
                '}';
    }

}
