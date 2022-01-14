package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CashTransactionRequest extends CardValidationRequest
{
    private final double cashTendered;

    public CashTransactionRequest(Long barcode, String number, double cashTendered)
    {
        super(barcode, number);
        this.cashTendered = cashTendered;
    }
}
