package com.example.LibraryManagement.models.io.requests.account_requests.member_requests;

import com.example.LibraryManagement.models.io.requests.account_requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CashTransactionRequest extends BarcodeValidationRequest
{
    @NotNull
    private double cashTendered;

    public CashTransactionRequest(String barcode, double cashTendered)
    {
        super(barcode);
        this.cashTendered = cashTendered;
    }
}
