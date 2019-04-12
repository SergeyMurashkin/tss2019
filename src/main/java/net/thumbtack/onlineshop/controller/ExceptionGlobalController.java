package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.model.OnlineShopException;
import net.thumbtack.onlineshop.dto.OnlineShopExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")

public class ExceptionGlobalController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public OnlineShopExceptionResponse handleFieldValidation(MethodArgumentNotValidException ex) {
        return new OnlineShopExceptionResponse(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OnlineShopException.class)
    @ResponseBody
    public OnlineShopExceptionResponse handleException(OnlineShopException ex) {

        return new OnlineShopExceptionResponse(ex);
    }





}
