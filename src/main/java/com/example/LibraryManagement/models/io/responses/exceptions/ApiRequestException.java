package com.example.LibraryManagement.models.io.responses.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 * REST API Exception:
 * Used during REST API requests to the backend application.
 * These exceptions are thrown during bad requests and will
 * contain the message describing the error in the request.
 */
@Getter
public class ApiRequestException extends RuntimeException
{
    HttpStatus status;
    public ApiRequestException(String message, HttpStatus status)
    {
        super(message);
        this.status = status;
    }
}
