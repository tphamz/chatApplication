package com.example.api_server.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.example.api_server.model.ErrorMessage;
import com.example.api_server.model.ValidationErrorMessage;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(Exception e, WebRequest request) {
        return this.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage accessDeniedException(AccessDeniedException e){
        return this.errorResponse(HttpStatus.FORBIDDEN.value(), "Access Denied", "");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage accessDeniedException(AuthenticationException e){
        return this.errorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid Access Token", "");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorMessage validationErrorException(MethodArgumentNotValidException e){
        return this.errorResponse(e.getBindingResult().getFieldErrors());
    }   


    private ErrorMessage errorResponse(int code, String message, String description){
        return new ErrorMessage(code, new Date(), message, description);
    }

    private ValidationErrorMessage errorResponse(List<FieldError> errors){
        ArrayList<String> messages = new ArrayList<String>();
        for(FieldError error:errors){
            messages.add(error.getDefaultMessage());
        }
        return new ValidationErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), messages);
    }

}
