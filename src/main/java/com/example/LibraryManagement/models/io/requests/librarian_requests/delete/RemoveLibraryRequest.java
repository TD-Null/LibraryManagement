package com.example.LibraryManagement.models.io.requests.librarian_requests.delete;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RemoveLibraryRequest extends CardValidationRequest
{
    @NotNull
    private final String library;

    public RemoveLibraryRequest(Long barcode, String number, String library)
    {
        super(barcode, number);
        this.library = library;
    }
}
