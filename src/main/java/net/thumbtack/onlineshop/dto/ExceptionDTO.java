package net.thumbtack.onlineshop.dto;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.onlineshop.model.OnlineShopException;

public class ExceptionDTO {

    private String errorCode;
    private String field;
    private String message;

    public ExceptionDTO() {

    }

    public ExceptionDTO(OnlineShopException ex) {
        this(ex.getErrorCode().name(), ex.getField(), ex.getMessage());
    }

    public ExceptionDTO(String errorCode, String field, String message) {
        this.errorCode = errorCode;
        this.field = field;
        this.message = message;
    }

    public ExceptionDTO(MySQLIntegrityConstraintViolationException ex) {
        this.errorCode = ex.getErrorCode() + "";
        this.message = ex.getLocalizedMessage();
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
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
