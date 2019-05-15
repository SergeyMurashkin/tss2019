package net.thumbtack.onlineshop.dto.requests;

import net.thumbtack.onlineshop.OnlineShopServer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddCategoryRequest {

    @NotBlank
    @Size(max=OnlineShopServer.MAX_NAME_LENGTH)
    private String name;

    @Min(0)
    private Integer parentId;

    public AddCategoryRequest() {
    }

    public AddCategoryRequest(String name, Integer parentId) {
        this.name = name;
        this.parentId = parentId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId==0?null:parentId;
    }

}
