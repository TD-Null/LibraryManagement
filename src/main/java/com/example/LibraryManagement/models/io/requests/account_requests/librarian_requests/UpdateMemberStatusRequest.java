package com.example.LibraryManagement.models.io.requests.account_requests.librarian_requests;

import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.io.requests.account_requests.BarcodeValidationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateMemberStatusRequest extends BarcodeValidationRequest
{
    @NotNull
    private final AccountStatus status;

    public UpdateMemberStatusRequest(Long barcode, AccountStatus status)
    {
        super(barcode);
        this.status = status;
    }
}
