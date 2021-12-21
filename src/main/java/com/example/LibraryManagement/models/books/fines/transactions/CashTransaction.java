package com.example.LibraryManagement.models.books.fines.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Type of transaction that uses cash to pay a fine.
@Getter
@AllArgsConstructor
public class CashTransaction
{
    private double cashTendered;
}
