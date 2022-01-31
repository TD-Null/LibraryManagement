package com.example.LibraryManagement.models.io.responses.exceptions;

import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.hibernate.JDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/*
 * Controller advice used to handle exceptions across the entire
 * application. The Exception Handlers used in this class will
 * catch exceptions globally during the application's runtime.
 *
 * Once an exception has been caught, a Response Entity will be
 * returned containing data of the REST API, including its message
 * and time that the exception was thrown, as well as return a given
 * HTTP Status code.
 */
@ControllerAdvice
public class ApiExceptionHandler
{
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e)
    {
        HttpStatus status = e.getStatus();
        ApiException apiException = new ApiException(
                new MessageResponse(e.getMessage()),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {JDBCConnectionException.class})
    public ResponseEntity<Object> handleJDBCConnectionException(JDBCException e)
    {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                new MessageResponse("Unable to connect to server: " +
                        "\"" + e.getMessage() + "\"."),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {CannotCreateTransactionException.class})
    public ResponseEntity<Object> handleCannotCreateTransactionException(CannotCreateTransactionException e)
    {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                new MessageResponse("Unable to create transactions to the server: " +
                        "\"" + e.getMessage() + "\"."),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {SQLException.class})
    public ResponseEntity<Object> handleSQLException(SQLException e)
    {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                new MessageResponse("Warnings or errors occurred when accessing the server: " +
                        "\"" + e.getMessage() + "\"."),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, status);
    }
}
