package com.example.LibraryManagement.models.io.requests;

import com.example.LibraryManagement.models.datatypes.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/*
 * Class containing the necessary inputs, being the user's details,
 * for a signup API request.
 */
@Getter
@AllArgsConstructor
public class SignupRequest
{
    @NotBlank
    private final String name;

    @NotBlank
    @Size(min = 6, max = 40)
    private final String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private final String email;

    @NotBlank
    private final String streetAddress;

    @NotBlank
    private final String city;

    @NotBlank
    private final String zipcode;

    @NotBlank
    private final String country;

    @NotBlank
    private final String phoneNumber;
}
