package com.example.LibraryManagement.models.io.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UpdateAccountRequest
{
    @NotBlank
    private String name;

    @NotBlank
    private String streetAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String zipcode;

    @NotBlank
    private String country;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;
}
