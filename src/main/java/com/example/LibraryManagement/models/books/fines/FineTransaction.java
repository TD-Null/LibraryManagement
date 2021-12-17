package com.example.LibraryManagement.models.books.fines;

import lombok.Data;

import java.sql.Date;

@Data
public class FineTransaction
{
    private Date creationDate;
    private double amount;
}
