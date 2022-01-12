package com.example.LibraryManagement.models.io.requests.librarian_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class SubjectRequest extends CardValidationRequest
{
    @NotBlank
    private final String subject;

    public SubjectRequest(Long barcode, @NotBlank String number, String subject)
    {
        super(barcode, number);
        this.subject = subject;
    }
}
