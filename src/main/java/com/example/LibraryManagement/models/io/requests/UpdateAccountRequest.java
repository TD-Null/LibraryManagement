package com.example.LibraryManagement.models.io.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateAccountRequest extends BarcodeValidationRequest
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

    public UpdateAccountRequest(Long barcode, String name, String streetAddress, String city,
                                String zipcode, String country, String email, String phoneNumber)
    {
        super(barcode);
        this.name = name;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
