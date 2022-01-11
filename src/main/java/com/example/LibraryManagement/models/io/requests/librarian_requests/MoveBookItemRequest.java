package com.example.LibraryManagement.models.io.requests.librarian_requests;

import com.example.LibraryManagement.models.io.requests.BarcodeValidationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class MoveBookItemRequest extends BarcodeValidationRequest
{
    @NotBlank
    private final String libraryName;

    private final int rack;

    @NotBlank
    private final String location;

    public MoveBookItemRequest(Long barcode, String libraryName, int rack, String location)
    {
        super(barcode);
        this.libraryName = libraryName;
        this.rack = rack;
        this.location = location;
    }
}
