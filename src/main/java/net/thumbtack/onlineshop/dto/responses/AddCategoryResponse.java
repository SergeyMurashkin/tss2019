package net.thumbtack.onlineshop.dto.responses;

public class AddCategoryResponse {

    private int id;
    private String name;
    private Integer parentId;
    private String parentName;

    public AddCategoryResponse(int id,
                               String name,
                               Integer parentId,
                               String parentName){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
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

}
