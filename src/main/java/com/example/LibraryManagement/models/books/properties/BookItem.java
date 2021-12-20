package com.example.LibraryManagement.models.books.properties;

import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table
public class BookItem extends Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Barcode")
    private String barcode;

    @Column(name = "Reference")
    private boolean isReferenceOnly;

    @Column(name = "Borrowed")
    private Date borrowed;

    @Column(name = "Due Date")
    private Date dueDate;

    @Column(name = "Price")
    private double price;

    @Column(name = "Format", nullable = false)
    private BookFormat format;

    @Column(name = "Status", nullable = false)
    private BookStatus status;

    @Column(name = "Date of Purchase")
    private Date dateOfPurchase;

    @Column(name = "Publication Date", nullable = false)
    private Date publicationDate;
}
