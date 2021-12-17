package com.example.LibraryManagement.models.books.fines.transactions;

import com.example.LibraryManagement.models.books.fines.FineTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CashTransaction extends FineTransaction
{
    private double cashTendered;
}
