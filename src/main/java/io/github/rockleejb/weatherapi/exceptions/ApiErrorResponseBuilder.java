package io.github.rockleejb.weatherapi.exceptions;

import org.springframework.http.HttpStatus;

public class ApiErrorResponseBuilder {
    private final String description;
    private String message;
    private HttpStatus httpStatus;
    private int statusCode;

    public ApiErrorResponseBuilder(String description) {
        this.description = description;
    }

    public ApiErrorResponseBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ApiErrorResponseBuilder httpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public ApiErrorResponseBuilder statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ApiErrorResponse build() {
        return new ApiErrorResponse(this.description, this.message, this.httpStatus, this.statusCode);
    }
}
