package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CancelMembershipRequest extends BarcodeValidationRequest
{
    @NotBlank
    private final String libraryCardNumber;
    @NotBlank
    private final String password;

    public CancelMembershipRequest(Long barcode, String libraryCardNumber, String password)
    {
        super(barcode);
        this.libraryCardNumber = libraryCardNumber;
        this.password = password;
    }
}
