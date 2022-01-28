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
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(barcode, number);
            cardValidationSuccess = true;

            ResponseEntity<Object> accountDetails = accountService.getAccountDetails(card, number);
            requestSuccess = true;
            return accountDetails;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            requestLog(requestType, httpServletRequest.getRequestURL().toString(),
                    barcode, number, cardValidationSuccess, requestSuccess, time);
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
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardNumberValidation(request.getLibraryCardNumber());
            ResponseEntity<LibraryCard> libraryCard = accountService.authenticateUser(card, request.getPassword());
            requestSuccess = true;
            return libraryCard;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            loginLog(requestType, httpServletRequest.getRequestURL().toString(),
                    request.getLibraryCardNumber(), request.getPassword(),
                    requestSuccess, time);
        }

    }

    /*
     * Registration POST request.
     * Expects a valid SignupRequest in the body including the user's details and password.
     * Returns a 200 response code with the details of the user's library card.
     */
    @PostMapping("/signup")
    public ResponseEntity<LibraryCard> signup(@Valid @RequestBody SignupRequest request)
    {
        log.trace("Method signup() called.");
        log.info("User is signing up for an account in the system.");
        return accountService.registerMember(request.getName(), request.getPassword(),
                request.getEmail(), request.getStreetAddress(),
                request.getCity(), request.getZipcode(),
                request.getCountry(), request.getPhoneNumber(),
                new Date());
    }

    /*
     * Account details update PUT request.
     * Expects a valid barcode and old/new details regarding the user's account,
     * for either a MEMBER or LIBRARIAN account.
     * Returns a 200 response code with a message indicating that the account's
     * details has been updated.
     */
    @PutMapping("/update")
    public ResponseEntity<MessageResponse> editAccountDetails(@Valid @RequestBody UpdateAccountRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        return accountService.updateAccountDetails(card, request.getNumber(),
                request.getName(), request.getStreetAddress(), request.getCity(),
                request.getZipcode(), request.getCountry(), request.getEmail(),
                request.getPhoneNumber());
    }

    /*
     * Account password update PUT request.
     * Expects a valid barcode and the original and new password regarding the
     * user's account, for either a MEMBER or LIBRARIAN account.
     * Returns a 200 response code with a message indicating that the account's
     * password has been updated.
     */
    @PutMapping("/update/password")
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        return accountService.changePassword(card,
                request.getOriginalPassword(),
                request.getNewPassword());
    }

    private void loginLog(String requestType, String requestURL, String number,
                          String password, boolean loginValidation, long time)
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
            successLog = "(Failure! Completed in " + time + " ms)";
        }

        log.info(requestType + " " + requestURL + " " + userLog + " " + successLog);
    }

    private void signupLog()
    {

    }

    private void requestLog(String requestType, String requestURL, long barcode,
                            String number, boolean cardValidation, boolean requestSuccess,
                            long time)
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

        log.info(requestType + " " + requestURL + " " + userLog + " " + successLog);
    }
}
