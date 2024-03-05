package io.github.rockleejb.weatherapi.exceptions;

import org.pmw.tinylog.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Exception> handleExceptions(Exception ex, WebRequest request) {
        Logger.error("Exception {} thrown for request {}", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }
}
