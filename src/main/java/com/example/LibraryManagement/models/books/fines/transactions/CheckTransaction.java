package com.example.LibraryManagement.models.books.fines.transactions;

import com.example.LibraryManagement.models.books.fines.FineTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckTransaction extends FineTransaction
{
    private String bankName;
    private String checkNumber;
}
