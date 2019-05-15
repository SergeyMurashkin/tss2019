package net.thumbtack.onlineshop.model;

public enum OnlineShopErrorCode {

    USER_ACCESS_PERMISSION("You do not have permissions for this operation."),
    USER_WRONG_LOGIN("Wrong login entered."),
    USER_LOGIN_DUPLICATE("The login is exists."),
    USER_WRONG_PASSWORD("Wrong password entered."),
    USER_WRONG_LOGIN_OR_PASSWORD("Wrong login or password entered."),
    USER_OLD_SESSION("Please, login again."),
    USER_NOT_ADMIN("Invalid user type, you are not administrator."),
    USER_NOT_CLIENT("Invalid user type, you are not client."),

    PRODUCT_NOT_EXISTS("Product is not exists."),
    PRODUCT_DUPLICATE("Product duplicate."),
    PRODUCT_ANOTHER_NAME("Product have another name. Now name is: "),
    PRODUCT_ANOTHER_PRICE("Product have another price. Now price is: "),
    PRODUCT_INSUFFICIENT_AMOUNT("Insufficient amount the product. In the store: "),
    PRODUCT_NOT_IN_BASKET("There is no such product in the basket."),
    BASKET_PRODUCT_DUPLICATE("This product is already in the basket."),
    PRODUCT_STATE_CHANGING ("The state of the product was changed during preparation. Try again."),

    PURCHASE_NOT_ADDED("Purchase not added. Try again."),

    CATEGORY_NOT_EXISTS("Category is not exists."),
    CATEGORY_HIERARCHY_VIOLATION("The category cannot be changed to a subcategory and vice versa."),
    CATEGORY_NAME_DUPLICATE("The category name already exists."),

    DEPOSIT_INCORRECT_VALUE("Incorrect type value deposit."),
    DEPOSIT_INSUFFICIENT_AMOUNT("Insufficient amount money on deposit."),
    DEPOSIT_STATE_CHANGING ("The state of the deposit was changed during preparation. Try again."),

    MESSAGE_NOT_READABLE("Error reading message. Don't use quotes (\") or backslash (\\)."),
    MESSAGE_MISSING_PART("Missing part of request."),

    PAGE_NOT_FOUND("The requested resource is not available or does not exist."),

    SERVER_PROBLEMS("The server is unable to respond.");




    private String errorText;

    OnlineShopErrorCode(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }


}
