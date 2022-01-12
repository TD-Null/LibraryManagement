package com.example.LibraryManagement.models.io.requests.librarian_requests.delete;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RemoveAuthorRequest extends CardValidationRequest
{
    @NotBlank
    private final String author;

    public RemoveAuthorRequest(@NotNull Long barcode, @NotBlank String number, String author)
    {
        super(barcode, number);
        this.author = author;
    }
}
