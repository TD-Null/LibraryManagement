package com.example.LibraryManagement.models.io.requests.librarian_requests.delete;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RemoveAuthorRequest extends CardValidationRequest
{
    @NotBlank(message = "Please give the name of the author.")
    private final String author;

    public RemoveAuthorRequest(Long barcode, String number, String author)
    {
        super(barcode, number);
        this.author = author;
    }
}
