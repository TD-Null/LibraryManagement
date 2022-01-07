package com.example.LibraryManagement.models.io.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/*
 * Class containing the necessary inputs, being the library card number
 * and password of the user's account, for a login API request.
 */
@Getter
@AllArgsConstructor
public class LoginRequest
{
    @NotBlank
    private final String libraryCardNumber;
    @NotBlank
    private final String password;
}
