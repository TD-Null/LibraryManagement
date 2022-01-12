package com.example.LibraryManagement.models.io.requests.librarian_requests.put;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class MoveBookItemRequest extends CardValidationRequest
{
    @NotNull
    private final Long bookBarcode;

    @NotBlank
    private final String libraryName;

    @NotNull
    private final int rack;

    @NotBlank
    private final String location;

    public MoveBookItemRequest(Long barcode, String number, Long bookBarcode, String libraryName, int rack, String location)
    {
        super(barcode, number);
        this.bookBarcode = bookBarcode;
        this.libraryName = libraryName;
        this.rack = rack;
        this.location = location;
    }
}
