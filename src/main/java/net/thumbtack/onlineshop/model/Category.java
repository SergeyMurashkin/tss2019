package net.thumbtack.onlineshop.model;

import java.util.List;

public class Category {

    private int id;
    private String name;
    private Integer parentId;
    private String parentName;
    private List<Category> childCategories;

    public Category(){
    }

    public Category(int id, String name, Integer parentId){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Category(int id, String name, List<Category> childCategories){
        this.id = id;
        this.name = name;
        this.childCategories = childCategories;
    }

    public Category(int id, String name, Integer parentId, String parentName, List<Category> childCategories){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
        this.childCategories = childCategories;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", childCategories=" + childCategories +
                '}';
    }
}
