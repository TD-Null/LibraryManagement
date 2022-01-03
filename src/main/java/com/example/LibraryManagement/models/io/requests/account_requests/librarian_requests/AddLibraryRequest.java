package com.example.LibraryManagement.models.io.requests.account_requests.librarian_requests;

import com.example.LibraryManagement.models.io.requests.account_requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AddLibraryRequest extends BarcodeValidationRequest
{
    @NotBlank
    private String libraryName;

    @NotBlank
    private String streetAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String zipcode;

    @NotBlank
    private String country;

    public AddLibraryRequest(String barcode, String libraryName, String streetAddress,
                             String city, String zipcode, String country)
    {
        super(barcode);
        this.libraryName = libraryName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
    }
}
