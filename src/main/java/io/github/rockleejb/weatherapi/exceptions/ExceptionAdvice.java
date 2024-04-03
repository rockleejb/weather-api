package io.github.rockleejb.weatherapi.exceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.pmw.tinylog.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @ExceptionHandler(value = {NumberFormatException.class})
    public final ResponseEntity<Map<String, Object>> handleNumberFormatException(Exception ex, WebRequest request) {
        Logger.error("NumberFormatException {} thrown for request {}", ex.getMessage(), request.getDescription(false));
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiErrorResponse response = new ApiErrorResponseBuilder("NumberFormatException")
                .message(ex.getMessage()).httpStatus(httpStatus).statusCode(400).build();
        return new ResponseEntity<>(convertApiErrorResponse(response), httpStatus);
    }

    @ExceptionHandler(value = {FileNotFoundException.class})
    public final ResponseEntity<Map<String, Object>> handleFileNotFoundException(Exception ex, WebRequest request) {
        Logger.error("FileNotFoundException {} thrown for request {}", ex.getMessage(), request.getDescription(false));
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiErrorResponse response = new ApiErrorResponseBuilder("FileNotFoundException")
                .message(ex.getMessage()).httpStatus(httpStatus).statusCode(404).build();
        return new ResponseEntity<>(convertApiErrorResponse(response), httpStatus);

    }

    @ExceptionHandler(value = {BadRequestException.class})
    public final ResponseEntity<Map<String, Object>> handleBadRequestException(Exception ex, WebRequest request) {
        Logger.error("BadRequestException {} thrown for request {}", ex.getMessage(), request.getDescription(false));
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiErrorResponse response = new ApiErrorResponseBuilder("BadRequestException")
                .message(ex.getMessage()).httpStatus(httpStatus).statusCode(400).build();
        return new ResponseEntity<>(convertApiErrorResponse(response), httpStatus);
    }

    private Map<String, Object> convertApiErrorResponse(ApiErrorResponse apiErrorResponse) {
        return objectMapper.convertValue(apiErrorResponse, new TypeReference<>() {});
    }
}
