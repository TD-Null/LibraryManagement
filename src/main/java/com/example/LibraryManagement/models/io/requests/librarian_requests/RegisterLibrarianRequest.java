package com.example.LibraryManagement.models.io.requests.librarian_requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class RegisterLibrarianRequest
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
