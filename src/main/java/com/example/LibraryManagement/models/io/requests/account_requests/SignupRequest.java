package com.example.LibraryManagement.models.io.requests.account_requests;

import com.example.LibraryManagement.models.datatypes.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@AllArgsConstructor
public class SignupRequest
{
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 6, max = 40)
    private final String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private final String email;

    @NotBlank
    private String streetAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String zipcode;

    @NotBlank
    private String country;

    @NotBlank
    private String phoneNumber;
}
