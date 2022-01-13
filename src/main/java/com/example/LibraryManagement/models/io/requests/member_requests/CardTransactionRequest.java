package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CardTransactionRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.cardTransactionMsg)
    private final String name;

    private final double amount;

    public CardTransactionRequest(Long barcode, String number, String name, double amount)
    {
        super(barcode, number);
        this.name = name;
        this.amount = amount;
    }
}
