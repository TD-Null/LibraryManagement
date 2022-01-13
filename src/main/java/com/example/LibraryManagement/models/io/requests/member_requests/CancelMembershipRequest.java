package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CancelMembershipRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.passwordMsg)
    private final String password;

    public CancelMembershipRequest(Long barcode, String number, String password)
    {
        super(barcode, number);
        this.password = password;
    }
}
