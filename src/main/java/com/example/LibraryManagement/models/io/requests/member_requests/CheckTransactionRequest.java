package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CheckTransactionRequest extends BarcodeValidationRequest
{
    @NotBlank
    private final String bankName;
    @NotBlank
    private final String checkNumber;
    @NotNull
    private final double amount;

    public CheckTransactionRequest(Long barcode, String bankName, String checkNumber, double amount)
    {
        super(barcode);
        this.bankName = bankName;
        this.checkNumber = checkNumber;
        this.amount = amount;
    }
}
