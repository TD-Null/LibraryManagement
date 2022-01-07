package com.example.LibraryManagement.models.io.requests.member_requests;

import com.example.LibraryManagement.models.io.requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CashTransactionRequest extends BarcodeValidationRequest
{
    @NotNull
    private double cashTendered;

    public CashTransactionRequest(Long barcode, double cashTendered)
    {
        super(barcode);
        this.cashTendered = cashTendered;
    }
}
