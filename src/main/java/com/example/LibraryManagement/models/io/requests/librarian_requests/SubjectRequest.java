package com.example.LibraryManagement.models.io.requests.librarian_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SubjectRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.subjectMsg)
    private final String subject;

    public SubjectRequest(Long barcode, @NotBlank String number, String subject)
    {
        super(barcode, number);
        this.subject = subject;
    }
}
