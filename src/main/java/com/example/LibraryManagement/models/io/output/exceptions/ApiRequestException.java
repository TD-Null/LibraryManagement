package com.example.LibraryManagement.models.io.output.exceptions;

/*
 * REST API Exception:
 * Used during REST API requests to the backend application.
 * These exceptions are thrown during bad requests and will
 * contain the message describing the error in the request.
 */
public class ApiRequestException extends RuntimeException
{
    public ApiRequestException(String message)
    {
        super(message);
    }
}
