package net.thumbtack.onlineshop.dto;

import net.thumbtack.onlineshop.dto.ExceptionDTO;
import net.thumbtack.onlineshop.model.OnlineShopException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

public class OnlineShopExceptionResponse {

    private List<ExceptionDTO> errors;

    public OnlineShopExceptionResponse(){

    }

    public OnlineShopExceptionResponse(MethodArgumentNotValidException ex) {
        errors = new ArrayList<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        for (ObjectError error : errors) {
            String errorCode = error.getCode();
            String field = ex.getBindingResult().getFieldError().getField();
            String message = error.getDefaultMessage();
            ExceptionDTO exceptionDTO = new ExceptionDTO(errorCode, field, message);
            this.errors.add(exceptionDTO);
        }
    }

    public OnlineShopExceptionResponse(OnlineShopException ex) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(ex);
        errors = new ArrayList<>();
        errors.add(exceptionDTO);
    }

    public List<ExceptionDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ExceptionDTO> errors) {
        this.errors = errors;
    }


}
