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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnlineShopException that = (OnlineShopException) o;

        if (errorCode != that.errorCode) return false;
        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result = errorCode != null ? errorCode.hashCode() : 0;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OnlineShopException{" +
                "errorCode=" + errorCode +
                ", field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
