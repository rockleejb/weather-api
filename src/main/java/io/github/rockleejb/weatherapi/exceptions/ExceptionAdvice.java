package io.github.rockleejb.weatherapi.exceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pmw.tinylog.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    @ExceptionHandler(value = {NumberFormatException.class})
    public final ResponseEntity<Map<String, Object>> handleNumberFormatException(Exception ex, WebRequest request) {
        Logger.error("NumberFormatException {} thrown for request {}", ex.getMessage(), request.getDescription(false));
        ErrorResponseDto errorResponseDto = new ErrorResponseDto("NumberFormatException", ex.getMessage(), HttpStatus.BAD_REQUEST, 400);
        return new ResponseEntity<>(convertErrorResponseDto(errorResponseDto), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> convertErrorResponseDto(ErrorResponseDto errorResponseDto) {
        return objectMapper.convertValue(errorResponseDto, new TypeReference<>() {});
    }
}
