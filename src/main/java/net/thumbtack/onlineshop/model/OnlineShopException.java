package net.thumbtack.onlineshop.model;

public class OnlineShopException extends  Exception{

    private OnlineShopErrorCode errorCode;
    private String field;
    private String message;

    public OnlineShopException(){

    }

    public OnlineShopException(OnlineShopErrorCode errorCode, String field, String message){
        this.errorCode = errorCode;
        this.field = field;
        this.message = message;
    }

    public OnlineShopErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(OnlineShopErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
