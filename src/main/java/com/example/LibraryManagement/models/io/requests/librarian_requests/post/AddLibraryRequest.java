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

    @NotBlank(message = ValidationMessages.streetMsg)
    private final String streetAddress;

    @NotBlank(message = ValidationMessages.cityMsg)
    private final String city;

    @NotBlank(message = ValidationMessages.zipcodeMsg)
    private final String zipcode;

    @NotBlank(message = ValidationMessages.countryMsg)
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
