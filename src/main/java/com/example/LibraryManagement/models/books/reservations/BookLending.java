package com.example.LibraryManagement.models.books.reservations;

import lombok.Data;

import java.sql.Date;

@Data
public class BookLending
{
    private Date creationDate;
    private Date dueDate;
    private Date returnDate;
}
