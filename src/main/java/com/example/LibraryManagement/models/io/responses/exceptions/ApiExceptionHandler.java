package com.example.LibraryManagement.models.io.responses.exceptions;

import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/*
 * Controller advice used to handle exceptions across the entire
 * application. The Exception Handler used in this class will
 * catch exclusively the ApiRequestException object exceptions.
 * Once an exception has caught, a Response Entity will be returned
 * containing data of the REST API, including its message and time
 * that the exception was thrown, as well as return an HTTP 400 Bad
 * Request code.
 */
@ControllerAdvice
public class ApiExceptionHandler
{
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e)
    {
        HttpStatus badRequest = e.getStatus();
        ApiException apiException = new ApiException(
                new MessageResponse(e.getMessage()),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, badRequest);
    }
}
