package com.example.LibraryManagement.models.io.requests.librarian_requests.post;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AddAuthorRequest extends CardValidationRequest
{
    @NotBlank(message = "Please give the name of the author.")
    private final String author;

    private final String description;

    public AddAuthorRequest(Long barcode, String number, String author, String description)
    {
        super(barcode, number);
        this.author = author;
        this.description = description;
    }
}
