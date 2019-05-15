package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.OnlineShopServer;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

public class EditProductRequest {

    @Size(max = OnlineShopServer.MAX_NAME_LENGTH)
    private String name;
    @Min(1)
    private Integer price;
    @Min(0)
    private Integer count;
    private List<@Min(1) Integer> categoriesId;


    public EditProductRequest() {
    }

    public EditProductRequest(String name, Integer price, Integer count, List<Integer> categoriesId) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.categoriesId = categoriesId;
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

    public List<Integer> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(List<Integer> categoriesId) {
        this.categoriesId = categoriesId;
    }

    @Override
    public String toString() {
        return "EditProductRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", categoriesId=" + categoriesId +
                '}';
    }


}
