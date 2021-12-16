package com.example.LibraryManagement.models.books.fines;

import com.example.LibraryManagement.models.enums.reservations.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FineTransaction
{
    private TransactionType type;
    private Object transaction;
}
