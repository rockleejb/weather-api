package io.github.rockleejb.weatherapi.exceptions;

import org.springframework.http.HttpStatus;

public record ApiErrorResponse(String description, String message, HttpStatus httpStatus, int statusCode) {
}
