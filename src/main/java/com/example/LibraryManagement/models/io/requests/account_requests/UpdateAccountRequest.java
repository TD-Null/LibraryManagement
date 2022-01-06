package com.example.LibraryManagement.models.io.requests.account_requests;

import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import lombok.Getter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class UpdateAccountRequest extends BarcodeValidationRequest
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

    public UpdateAccountRequest(Long barcode, String name, String streetAddress,
                                String city, String zipcode, String country, String email,
                                String phoneNumber)
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
