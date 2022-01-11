package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CardTransactionRequest extends BarcodeValidationRequest
{
    @NotBlank
    private final String name;
    @NotNull
    private final double amount;

    public CardTransactionRequest(Long barcode, String name, double amount)
    {
        super(barcode);
        this.name = name;
        this.amount = amount;
    }
}
