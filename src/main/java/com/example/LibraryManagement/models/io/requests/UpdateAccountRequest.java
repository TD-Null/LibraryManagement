package com.example.LibraryManagement.models.io.requests;

import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UpdateAccountRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.nameMsg)
    private final String name;

    @NotBlank(message = ValidationMessages.emailMsg)
    @Size(max = 50, message = ValidationMessages.emailSizeMsg)
    @Email(message = ValidationMessages.emailValidMsg)
    private final String email;

    @NotBlank(message = ValidationMessages.streetMsg)
    private final String streetAddress;

    @NotBlank(message = ValidationMessages.cityMsg)
    private final String city;

    @NotBlank(message = ValidationMessages.zipcodeMsg)
    private final String zipcode;

    @NotBlank(message = ValidationMessages.countryMsg)
    private final String country;

    @NotBlank(message = ValidationMessages.phoneMsg)
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
