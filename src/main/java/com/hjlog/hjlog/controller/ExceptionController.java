package com.hjlog.hjlog.controller;

import com.hjlog.hjlog.exception.HjlogException;
import com.hjlog.hjlog.exception.InvalidRequest;
import com.hjlog.hjlog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.hjlog.hjlog.response.ErrorResponse.*;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

        ErrorResponse response = builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for(FieldError error : e.getFieldErrors()) {
            response.addValidation(error.getField(), error.getDefaultMessage());
        }

        return response;
    }

    @ExceptionHandler(HjlogException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> hjlogException(HjlogException e) {

        int statusCode = e.getStatusCode();
        ErrorResponse responseBody = builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode)
                .body(responseBody);
    }
}
