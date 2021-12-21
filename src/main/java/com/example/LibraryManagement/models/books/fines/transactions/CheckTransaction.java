package com.example.LibraryManagement.models.books.fines.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

// Type of transaction that uses a check to pay a fine.
@Getter
@AllArgsConstructor
public class CheckTransaction
{
    private String bankName;
    private String checkNumber;
}
