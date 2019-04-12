package net.thumbtack.onlineshop.model;

public enum OnlineShopErrorCode {


    USER_WRONG_LOGIN("Wrong login entered."),
    USER_WRONG_PASSWORD("Wrong password entered."),
    USER_OLD_SESSION("Please, login again."),
    USER_NOT_ADMIN("Invalid user type, you are not administrator"),
    USER_NOT_CLIENT("Invalid user type, you are not client"),
    CATEGORY_NOT_EXISTS("Category is not exists"),
    CATEGORY_NAME_DUPLICATE("The category name already exists");

    private String errorText;

    OnlineShopErrorCode(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }


}
