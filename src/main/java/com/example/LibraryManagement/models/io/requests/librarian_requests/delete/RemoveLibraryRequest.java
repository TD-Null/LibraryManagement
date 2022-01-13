package com.example.LibraryManagement.models.io.requests.librarian_requests.delete;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class RemoveLibraryRequest extends CardValidationRequest
{
    @NotBlank(message = "Please give the name of the library.")
    private final String library;

    public RemoveLibraryRequest(Long barcode, String number, String library)
    {
        super(barcode, number);
        this.library = library;
    }
}
