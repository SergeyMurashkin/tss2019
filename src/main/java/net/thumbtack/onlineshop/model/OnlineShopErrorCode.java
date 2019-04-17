package net.thumbtack.onlineshop.model;

public enum OnlineShopErrorCode {


    USER_ACCESS_PERMISSION("You do not have permissions for this operation."),
    USER_WRONG_LOGIN("Wrong login entered."),
    USER_WRONG_PASSWORD("Wrong password entered."),
    USER_OLD_SESSION("Please, login again."),
    USER_NOT_ADMIN("Invalid user type, you are not administrator."),
    USER_NOT_CLIENT("Invalid user type, you are not client."),

    PRODUCT_NOT_EXISTS("Product is not exists."),
    PRODUCT_DUPLICATE("Product duplicate."),
    PRODUCT_ANOTHER_NAME("Product have another name. Now name is: "),
    PRODUCT_ANOTHER_PRICE("Product have another price. Now price is: "),
    PRODUCT_INSUFFICIENT_AMOUNT("Insufficient amount the product. In the store: "),

    CATEGORY_NOT_EXISTS("Category is not exists."),
    CATEGORY_NAME_DUPLICATE("The category name already exists."),

    DEPOSIT_INCORRECT_VALUE("Incorrect type value deposit."),
    DEPOSIT_INSUFFICIENT_AMOUNT("Insufficient amount money on deposit.");

    private String errorText;

    OnlineShopErrorCode(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }


}
