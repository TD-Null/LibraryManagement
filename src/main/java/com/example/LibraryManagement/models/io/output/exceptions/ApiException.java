package com.example.LibraryManagement.models.io.output.exceptions;

import com.example.LibraryManagement.models.io.output.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/*
 * Contains the data of the REST API exception. This will include
 * the message from the ApiRequestException object's message, as
 * well as the HTTP status (usually an HTTP 400 Bad Request code)
 * and the current time when the exception was thrown.
 */
@Getter
@AllArgsConstructor
public class ApiException
{
    private final MessageResponse message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
