package com.example.LibraryManagement.models.io.requests;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ChangePasswordRequest extends CardValidationRequest
{
    @NotBlank
    private final String originalPassword;

    @NotBlank
    private final String newPassword;

    public ChangePasswordRequest(Long barcode, String number, String originalPassword, String newPassword)
    {
        super(barcode, number);
        this.originalPassword = originalPassword;
        this.newPassword = newPassword;
    }
}
