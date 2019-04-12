package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.model.Category;

public class AddCategoryRequest {

    String name;
    Integer parentId;

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

}
