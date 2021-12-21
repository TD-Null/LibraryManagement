package com.example.LibraryManagement.models.books.actions;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Data
@Entity
@Table
public class BookLending
{
    private Date creationDate;
    private Date dueDate;
    private Date returnDate;
}
