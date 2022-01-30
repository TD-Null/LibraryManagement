package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.io.requests.*;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/*
 * Controller component containing the API requests relating to accounts:
 *
 * - Logging in
 * - Signing up for a new account
 * - Viewing user's account details
 * - Edit user's account details
 * - Change user's password
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("library_website/account")
public class AccountController
{
    @Autowired
    private final AccountServiceImp accountService;
    @Autowired
    private final ValidationService validationService;

    /*
     * Account details GET request.
     * Expects a valid barcode from the user's library card.
     * Returns a 200 response code with the details of the user's accounts,
     * from either a MEMBER or LIBRARIAN account.
     */
    @GetMapping("/details")
    public ResponseEntity<Object> viewAccountDetails(HttpServletRequest httpServletRequest,
                                                     @RequestParam(value = "barcode") Long barcode,
                                                     @RequestParam(value = "card") String number)
    {
        String requestType = "GET";
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        ResponseEntity<Object> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(barcode, number);
            cardValidationSuccess = true;

            response = accountService.getAccountDetails(card, number);
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message = "";

            if(requestSuccess)
                message = "User has obtained their account details.";

            else
                message = "User was unable to obtain their account details.";

            requestLog(requestType, httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    /*
     * Authentication POST request.
     * Expects a valid LoginRequest in the body including the user's library card
     * number and password.
     * Returns a 200 response code with the details of the user's library card.
     */
    @PostMapping("/login")
    public ResponseEntity<LibraryCard> login(HttpServletRequest httpServletRequest,
                                             @Valid @RequestBody LoginRequest request)
    {
        String requestType = "POST";
        boolean requestSuccess = false;
        ResponseEntity<LibraryCard> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardNumberValidation(request.getLibraryCardNumber());
            response = accountService.authenticateUser(card, request.getPassword());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message = "";

            if(requestSuccess)
                message = "User has login to their account.";

            else
                message = "User failed to login to their account (Either wrong library card number or password).";

            loginLog(requestType, httpServletRequest.getRequestURL().toString(),
                    message, request.getLibraryCardNumber(), request.getPassword(),
                    requestSuccess, time);
        }

    }

    /*
     * Registration POST request.
     * Expects a valid SignupRequest in the body including the user's details and password.
     * Returns a 200 response code with the details of the user's library card.
     */
    @PostMapping("/signup")
    public ResponseEntity<LibraryCard> signup(HttpServletRequest httpServletRequest,
                                              @Valid @RequestBody SignupRequest request)
    {
        String requestType = "POST";
        boolean requestSuccess = false;
        ResponseEntity<LibraryCard> response;
        Instant start = Instant.now();

        try
        {
            response = accountService.registerMember(request.getName(),
                    request.getPassword(), request.getEmail(), request.getStreetAddress(),
                    request.getCity(), request.getZipcode(), request.getCountry(),
                    request.getPhoneNumber(), new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message = "";

            if(requestSuccess)
                message = "User has created their account.";

            else
                message = "User failed to create their account.";

            signupLog(requestType, httpServletRequest.getRequestURL().toString(),
                    message, request.getName(), request.getPassword(), request.getEmail(),
                    requestSuccess, time);
        }
    }

    /*
     * Account details update PUT request.
     * Expects a valid barcode and old/new details regarding the user's account,
     * for either a MEMBER or LIBRARIAN account.
     * Returns a 200 response code with a message indicating that the account's
     * details has been updated.
     */
    @PutMapping("/update")
    public ResponseEntity<MessageResponse> editAccountDetails(HttpServletRequest httpServletRequest,
                                                              @Valid @RequestBody UpdateAccountRequest request)
    {
        String requestType = "PUT";
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            cardValidationSuccess = true;

            response = accountService.updateAccountDetails(card, request.getNumber(),
                    request.getName(), request.getStreetAddress(), request.getCity(),
                    request.getZipcode(), request.getCountry(), request.getEmail(),
                    request.getPhoneNumber());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message = "";

            if(requestSuccess)
                message = "User has successfully edited their account details.";

            else
                message = "User has failed to edit their account details.";

            requestLog(requestType, httpServletRequest.getRequestURL().toString(), message,
                    request.getBarcode(), request.getNumber(), cardValidationSuccess,
                    requestSuccess, time);
        }
    }

    /*
     * Account password update PUT request.
     * Expects a valid barcode and the original and new password regarding the
     * user's account, for either a MEMBER or LIBRARIAN account.
     * Returns a 200 response code with a message indicating that the account's
     * password has been updated.
     */
    @PutMapping("/update/password")
    public ResponseEntity<MessageResponse> changePassword(HttpServletRequest httpServletRequest,
                                                          @Valid @RequestBody ChangePasswordRequest request)
    {
        String requestType = "PUT";
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            cardValidationSuccess = true;

            response = accountService.changePassword(card,
                    request.getOriginalPassword(),
                    request.getNewPassword());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message = "";

            if(requestSuccess)
                message = "User has successfully changed their password.";

            else
                message = "User has failed to change their password.";

            requestLog(requestType, httpServletRequest.getRequestURL().toString(), message,
                    request.getBarcode(), request.getNumber(), cardValidationSuccess,
                    requestSuccess, time);
        }
    }

    private void loginLog(String requestType, String requestURL, String message,
                          String number, String password, boolean loginValidation,
                          long time)
    {
        String userLog = "(Login user credentials:" +
                " Number = " + number +
                ", Password = " + password;
        String successLog;

        if(loginValidation)
        {
            userLog += " [Valid])";
            successLog = "(Success! Completed in " + time + " ms)";
        }

        else
        {
            userLog += " [Invalid])";
            successLog = "(Failed! Completed in " + time + " ms)";
        }

        log.info(requestType + " " + requestURL + " " + message + " " + userLog + " " + successLog);
    }

    private void signupLog(String requestType, String requestURL, String message,
                           String name, String password, String email, boolean loginValidation,
                           long time)
    {
        String userLog = "(Login user credentials:" +
                " Name = " + name +
                ", Password = " + password +
                ", Email = " + email;
        String successLog;

        if(loginValidation)
        {
            userLog += " [Valid])";
            successLog = "(Success! Completed in " + time + " ms)";
        }

        else
        {
            userLog += " [Invalid])";
            successLog = "(Failed! Completed in " + time + " ms)";
        }

        log.info(requestType + " " + requestURL + " " + message + " " + userLog + " " + successLog);
    }

    private void requestLog(String requestType, String requestURL, String message,
                            long barcode, String number, boolean cardValidation,
                            boolean requestSuccess, long time)
    {
        String userLog = "(User:" +
                " Barcode = " + barcode +
                ", Number = " + number;
        String successLog;

        if(cardValidation)
            userLog += " [Valid])";

        else
            userLog += " [Invalid])";

        if(requestSuccess)
            successLog = "(Success! Completed in " + time + " ms)";

        else
            successLog = "(Failure! Completed in " + time + " ms)";

        log.info(requestType + " " + requestURL + " " + message + " " + userLog + " " + successLog);
    }
}
