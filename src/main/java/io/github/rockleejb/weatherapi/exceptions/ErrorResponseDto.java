package io.github.rockleejb.weatherapi.exceptions;

import org.springframework.http.HttpStatus;

public class ErrorResponseDto {
    private String description;
    private String message;
    private HttpStatus httpStatus;
    private int statusCode;

    public ErrorResponseDto(String description, String message, HttpStatus httpStatus, int statusCode) {
        this.description = description;
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
