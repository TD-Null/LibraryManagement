package com.example.LibraryManagement.models.io.requests;

import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ChangePasswordRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.passwordMsg)
    private final String originalPassword;

    @NotBlank(message = ValidationMessages.newPasswordMsg)
    @Size(min = 6, max = 40, message = ValidationMessages.passwordSizeMsg)
    private final String newPassword;

    public ChangePasswordRequest(Long barcode, String number, String originalPassword, String newPassword)
    {
        super(barcode, number);
        this.originalPassword = originalPassword;
        this.newPassword = newPassword;
    }
}
