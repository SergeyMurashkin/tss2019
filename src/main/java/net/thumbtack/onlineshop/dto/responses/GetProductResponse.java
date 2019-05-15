package net.thumbtack.onlineshop.dto.responses;

import java.util.List;

public class GetProductResponse {

    private int id;
    private String name;
    private int price;
    private int count;
    private List<String> categories;

    public GetProductResponse(){
    }

    public GetProductResponse(int id, String name, int price, int count, List<String> categories){
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetProductResponse that = (GetProductResponse) o;

        if (id != that.id) return false;
        if (price != that.price) return false;
        if (count != that.count) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return categories != null ? categories.equals(that.categories) : that.categories == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + count;
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        return result;
    }

}
