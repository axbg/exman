package com.web.poc1.config;

import com.web.poc1.exception.CustomException;
import com.web.poc1.util.MessageHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<MessageHolder> handleException(CustomException ex) {
        return new ResponseEntity<>(new MessageHolder(ex.getMessage()), ex.getStatus());
    }

}
