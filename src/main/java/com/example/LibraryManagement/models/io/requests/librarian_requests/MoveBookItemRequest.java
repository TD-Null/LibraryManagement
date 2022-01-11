package com.example.LibraryManagement.models.io.requests.librarian_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class MoveBookItemRequest extends CardValidationRequest
{
    @NotBlank
    private final String libraryName;

    private final int rack;

    @NotBlank
    private final String location;

    public MoveBookItemRequest(Long barcode, String number, String libraryName, int rack, String location)
    {
        super(barcode, number);
        this.libraryName = libraryName;
        this.rack = rack;
        this.location = location;
    }
}
