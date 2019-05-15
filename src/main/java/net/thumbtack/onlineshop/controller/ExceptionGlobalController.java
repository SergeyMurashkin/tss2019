package net.thumbtack.onlineshop.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.onlineshop.dto.OnlineShopExceptionResponse;
import net.thumbtack.onlineshop.model.OnlineShopErrorCode;
import net.thumbtack.onlineshop.model.OnlineShopException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;


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


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public OnlineShopExceptionResponse httpMessageNotReadableException() {
        return new OnlineShopExceptionResponse(
                new OnlineShopException(
                        OnlineShopErrorCode.MESSAGE_NOT_READABLE,
                        null,
                        OnlineShopErrorCode.MESSAGE_NOT_READABLE.getErrorText()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            IllegalArgumentException.class})
    public OnlineShopExceptionResponse missingRequestPartException() {
        return new OnlineShopExceptionResponse(
                new OnlineShopException(
                        OnlineShopErrorCode.MESSAGE_MISSING_PART,
                        null,
                        OnlineShopErrorCode.MESSAGE_MISSING_PART.getErrorText()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public OnlineShopExceptionResponse noHandlerFoundException() {
        return new OnlineShopExceptionResponse(
                new OnlineShopException(
                        OnlineShopErrorCode.PAGE_NOT_FOUND,
                        "address line",
                        OnlineShopErrorCode.PAGE_NOT_FOUND.getErrorText()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public OnlineShopExceptionResponse serverException() {
        return new OnlineShopExceptionResponse(
                new OnlineShopException(
                        OnlineShopErrorCode.SERVER_PROBLEMS,
                        null,
                        OnlineShopErrorCode.SERVER_PROBLEMS.getErrorText()));
    }


}
