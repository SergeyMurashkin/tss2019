package net.thumbtack.onlineshop.model;

import java.util.List;

public class Product {

    private int id;
    private String name;
    private int price;
    private int count;
    private List<Category> categories;
    private Integer version;

    public Product(){
    }

    public Product(int id, String name, int price, int count, List<Category> categories, Integer version){
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
        this.version = version;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", categories=" + categories +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (getId() != product.getId()) return false;
        if (getPrice() != product.getPrice()) return false;
        if (getCount() != product.getCount()) return false;
        if (getName() != null ? !getName().equals(product.getName()) : product.getName() != null) return false;
        if (getCategories() != null ? !getCategories().equals(product.getCategories()) : product.getCategories() != null)
            return false;
        return getVersion() != null ? getVersion().equals(product.getVersion()) : product.getVersion() == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + price;
        return result;
    }
}
