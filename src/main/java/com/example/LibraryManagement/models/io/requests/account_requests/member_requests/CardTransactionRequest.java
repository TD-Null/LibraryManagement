package com.example.LibraryManagement.models.io.requests.account_requests.member_requests;

import com.example.LibraryManagement.models.io.requests.account_requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CardTransactionRequest extends BarcodeValidationRequest
{
    @NotBlank
    private String name;
    @NotNull
    private double amount;

    public CardTransactionRequest(@NotBlank String barcode, String name, double amount)
    {
        super(barcode);
        this.name = name;
        this.amount = amount;
    }
}
