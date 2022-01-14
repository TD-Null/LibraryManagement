package com.example.LibraryManagement.models.io.requests.librarian_requests.put;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateAuthorRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.authorMsg)
    private final String author;

    @NotBlank(message = ValidationMessages.authorDescMsg)
    private final String description;

    public UpdateAuthorRequest(Long barcode, String number, String author, String description)
    {
        super(barcode, number);
        this.author = author;
        this.description = description;
    }
}
