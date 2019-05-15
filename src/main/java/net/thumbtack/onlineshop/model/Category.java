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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (getId() != category.getId()) return false;
        if (getName() != null ? !getName().equals(category.getName()) : category.getName() != null) return false;
        if (getParentId() != null ? !getParentId().equals(category.getParentId()) : category.getParentId() != null)
            return false;
        if (getParentName() != null ? !getParentName().equals(category.getParentName()) : category.getParentName() != null)
            return false;
        return getChildCategories() != null ? getChildCategories().equals(category.getChildCategories()) : category.getChildCategories() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getParentId() != null ? getParentId().hashCode() : 0);
        result = 31 * result + (getParentName() != null ? getParentName().hashCode() : 0);
        result = 31 * result + (getChildCategories() != null ? getChildCategories().hashCode() : 0);
        return result;
    }
}
