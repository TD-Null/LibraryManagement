package com.example.LibraryManagement.models.books.fines.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

// Type of transaction that uses cash to pay a fine.
@Getter
@AllArgsConstructor
public class CashTransaction
{
    @NotNull
    private double cashTendered;
}
