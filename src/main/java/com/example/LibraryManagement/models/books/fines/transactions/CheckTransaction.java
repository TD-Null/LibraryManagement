package com.example.LibraryManagement.models.books.fines.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

// Type of transaction that uses a check to pay a fine.
@Getter
@AllArgsConstructor
public class CheckTransaction
{
    @NotBlank
    private String bankName;

    @NotBlank
    private String checkNumber;
}
