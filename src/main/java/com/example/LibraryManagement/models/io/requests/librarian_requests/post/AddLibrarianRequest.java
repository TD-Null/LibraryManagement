package com.example.LibraryManagement.models.io.requests.librarian_requests.post;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class AddLibrarianRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.nameMsg)
    private final String name;

    @NotBlank(message = ValidationMessages.passwordMsg)
    @Size(min = 6, max = 40, message = ValidationMessages.passwordSizeMsg)
    private final String password;

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

    public AddLibrarianRequest(Long barcode, String number, String name, String password,
                               String email, String streetAddress, String city,
                               String zipcode, String country, String phoneNumber)
    {
        super(barcode, number);
        this.name = name;
        this.password = password;
        this.email = email;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }
}
