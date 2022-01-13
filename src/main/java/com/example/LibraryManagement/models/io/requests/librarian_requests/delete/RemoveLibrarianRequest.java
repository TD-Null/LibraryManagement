package com.example.LibraryManagement.models.io.requests.librarian_requests.delete;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RemoveLibrarianRequest extends CardValidationRequest
{
    @NotNull(message = "Please give the barcode of the library card.")
    private final Long librarianCardBarcode;

    @NotBlank(message = "Please give the card number of the library card.")
    private final String librarianCardNumber;

    public RemoveLibrarianRequest(Long barcode, String number,
                                  Long librarianCardBarcode, String librarianCardNumber)
    {
        super(barcode, number);
        this.librarianCardBarcode = librarianCardBarcode;
        this.librarianCardNumber = librarianCardNumber;
    }
}
