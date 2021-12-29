package com.example.LibraryManagement.models.io.requests.account_requests.librarian_requests;

import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UpdateMemberStatusRequest
{
    @NotBlank
    private final String barcode;

    @NotNull
    private final AccountStatus status;
}
