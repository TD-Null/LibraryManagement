package com.example.LibraryManagement.models.books.fines.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckTransaction
{
    private String bankName;
    private String checkNumber;
}
