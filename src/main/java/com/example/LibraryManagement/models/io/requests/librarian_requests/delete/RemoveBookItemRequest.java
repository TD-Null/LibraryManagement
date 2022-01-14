package com.example.LibraryManagement.models.io.requests.librarian_requests.delete;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RemoveBookItemRequest extends CardValidationRequest
{
    @NotNull(message = "Please give the barcode of the book.")
    private final Long bookBarcode;

    public RemoveBookItemRequest(Long barcode, String number, Long bookBarcode)
    {
        super(barcode, number);
        this.bookBarcode = bookBarcode;
    }
}
