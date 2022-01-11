package com.example.LibraryManagement.models.io.requests.librarian_requests;

import com.example.LibraryManagement.models.io.requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class AddLibrarianRequest extends BarcodeValidationRequest
{
    @NotBlank
    private final String name;

    @NotBlank
    @Size(min = 6, max = 40)
    private final String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private final String email;

    @NotBlank
    private final String streetAddress;

    @NotBlank
    private final String city;

    @NotBlank
    private final String zipcode;

    @NotBlank
    private final String country;

    @NotBlank
    private final String phoneNumber;

    public AddLibrarianRequest(Long barcode, String name, String password,
                               String email, String streetAddress, String city,
                               String zipcode, String country, String phoneNumber)
    {
        super(barcode);
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
