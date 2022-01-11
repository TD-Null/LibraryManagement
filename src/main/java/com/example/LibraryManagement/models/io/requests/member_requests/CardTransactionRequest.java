package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CardTransactionRequest extends CardValidationRequest
{
    @NotBlank
    private final String name;
    @NotNull
    private final double amount;

    public CardTransactionRequest(Long barcode, String number, String name, double amount)
    {
        super(barcode, number);
        this.name = name;
        this.amount = amount;
    }
}
