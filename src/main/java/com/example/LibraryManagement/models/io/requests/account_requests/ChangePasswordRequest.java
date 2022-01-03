package com.example.LibraryManagement.models.io.requests.account_requests;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ChangePasswordRequest extends BarcodeValidationRequest
{
    @NotBlank
    private String originalPassword;

    @NotBlank
    private String newPassword;

    public ChangePasswordRequest(String barcode, String originalPassword, String newPassword)
    {
        super(barcode);
        this.originalPassword = originalPassword;
        this.newPassword = newPassword;
    }
}
