package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.io.requests.*;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;
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
    public ResponseEntity<Object> viewAccountDetails(@RequestParam(value = "barcode") Long barcode,
                                                     @RequestParam(value = "card") String number)
    {
        LibraryCard card = validationService.cardValidation(barcode);
        return accountService.getAccountDetails(card, number);
    }

    /*
     * Authentication POST request.
     * Expects a valid LoginRequest in the body including the user's library card
     * number and password.
     * Returns a 200 response code with the details of the user's library card.
     */
    @PostMapping("/login")
    public ResponseEntity<LibraryCard> login(@Valid @RequestBody LoginRequest loginRequest)
    {
        LibraryCard card = validationService.cardNumberValidation(loginRequest.getLibraryCardNumber());
        return accountService.authenticateUser(card,
                loginRequest.getPassword());
    }

    /*
     * Registration POST request.
     * Expects a valid SignupRequest in the body including the user's details and password.
     * Returns a 200 response code with the details of the user's library card.
     */
    @PostMapping("/signup")
    public ResponseEntity<LibraryCard> signup(@Valid @RequestBody SignupRequest signUpRequest)
    {
        return accountService.registerMember(signUpRequest.getName(), signUpRequest.getPassword(),
                signUpRequest.getEmail(), signUpRequest.getStreetAddress(),
                signUpRequest.getCity(), signUpRequest.getZipcode(),
                signUpRequest.getCountry(), signUpRequest.getPhoneNumber(), new Date());
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
        LibraryCard card = validationService.cardValidation(request.getBarcode());
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
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        return accountService.changePassword(card, request.getOriginalPassword(), request.getNewPassword());
    }
}
