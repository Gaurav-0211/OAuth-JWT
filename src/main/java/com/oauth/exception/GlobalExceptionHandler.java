package com.oauth.exception;

import com.oauth.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Response response = Response.buildResponse(
                    "FAILED",
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST.value(),
                    null,
                    "Executed success"
            );

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(NoUserExist.class)
    public ResponseEntity<Response> handleNoUserExistException(NoUserExist ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                null,
                "User Not Found With this Id"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<Response> alreadyUserExist(UserAlreadyExist ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                null,
                "User already exist with this Id"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));
    }

}
