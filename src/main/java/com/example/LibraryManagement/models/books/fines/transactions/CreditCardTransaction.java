package com.example.LibraryManagement.models.books.fines.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

// Type of transaction that uses a credit card to pay a fine.
@Getter
@AllArgsConstructor
public class CreditCardTransaction
{
    @NotBlank
    private String nameOnCard;
}
