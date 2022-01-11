package com.example.LibraryManagement.models.io.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ChangePasswordRequest extends BarcodeValidationRequest
{
    @NotBlank
    private final String originalPassword;

    @NotBlank
    private final String newPassword;

    public ChangePasswordRequest(Long barcode, String originalPassword, String newPassword)
    {
        super(barcode);
        this.originalPassword = originalPassword;
        this.newPassword = newPassword;
    }
}
