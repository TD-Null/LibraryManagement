package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CheckTransactionRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.checkBankMsg)
    private final String bankName;

    @NotBlank(message = ValidationMessages.checkNumberMsg)

    private final String checkNumber;

    private final double amount;

    public CheckTransactionRequest(Long barcode, String number, String bankName, String checkNumber, double amount)
    {
        super(barcode, number);
        this.bankName = bankName;
        this.checkNumber = checkNumber;
        this.amount = amount;
    }
}
