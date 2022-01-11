package com.example.LibraryManagement.models.io.requests;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateAccountRequest extends CardValidationRequest
{
    @NotBlank
    private final String name;

    @NotBlank
    private final String streetAddress;

    @NotBlank
    private final String city;

    @NotBlank
    private final String zipcode;

    @NotBlank
    private final String country;

    @NotBlank
    private final String email;

    @NotBlank
    private final String phoneNumber;

    public UpdateAccountRequest(Long barcode, String number, String name, String streetAddress, String city,
                                String zipcode, String country, String email, String phoneNumber)
    {
        super(barcode, number);
        this.name = name;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
