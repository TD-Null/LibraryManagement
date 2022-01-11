package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CheckTransactionRequest extends CardValidationRequest
{
    @NotBlank
    private final String bankName;
    @NotBlank
    private final String checkNumber;
    @NotNull
    private final double amount;

    public CheckTransactionRequest(Long barcode, String number, String bankName, String checkNumber, double amount)
    {
        super(barcode, number);
        this.bankName = bankName;
        this.checkNumber = checkNumber;
        this.amount = amount;
    }
}
