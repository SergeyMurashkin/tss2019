package net.thumbtack.onlineshop.dto.requests;

import java.util.List;

public class AddProductRequest {

    private String name;
    private Integer price;
    private Integer count;
    private List<Integer> categories;

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

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategoriesId(List<Integer> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "AddProductRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", categories=" + categories +
                '}';
    }
}
