package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CancelMembershipRequest extends CardValidationRequest
{
    @NotBlank
    private final String password;

    public CancelMembershipRequest(Long barcode, String number, String password)
    {
        super(barcode, number);
        this.password = password;
    }
}
