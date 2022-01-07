package com.example.LibraryManagement.models.io.requests.account_requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class ChangePasswordRequest
{
    @NotBlank
    private String originalPassword;

    @NotBlank
    private String newPassword;

//    public ChangePasswordRequest(Long barcode, String originalPassword, String newPassword)
//    {
//        this.originalPassword = originalPassword;
//        this.newPassword = newPassword;
//    }
}
