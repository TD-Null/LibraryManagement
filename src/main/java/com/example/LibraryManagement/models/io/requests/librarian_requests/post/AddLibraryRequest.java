package com.example.LibraryManagement.models.io.requests.librarian_requests.post;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AddLibraryRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.libraryMsg)
    private final String libraryName;

    @NotBlank
    private final String streetAddress;

    @NotBlank
    private final String city;

    @NotBlank
    private final String zipcode;

    @NotBlank
    private final String country;

    public AddLibraryRequest(Long barcode, String number, String libraryName, String streetAddress,
                             String city, String zipcode, String country)
    {
        super(barcode, number);
        this.libraryName = libraryName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
    }
}
