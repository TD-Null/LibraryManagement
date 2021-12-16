package com.example.LibraryManagement.models.books.reservations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class BookLending
{
    private Date creationDate;
    private Date dueDate;
    private Date returnDate;
}
