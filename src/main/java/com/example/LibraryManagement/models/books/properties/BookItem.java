package com.example.LibraryManagement.models.books.properties;

import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@MappedSuperclass
@Data
public class BookItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Barcode")
    private String barcode;

    @NotBlank
    @Column(name = "Reference")
    private boolean isReferenceOnly;

    @Column(name = "Borrowed")
    private Date borrowed;

    @Column(name = "Due Date")
    private Date dueDate;

    @Column(name = "Price")
    private double price;

    @NotBlank
    @Column(name = "Format")
    private BookFormat format;

    @NotBlank
    @Column(name = "Status")
    private BookStatus status;

    @Column(name = "Date of Purchase")
    private Date dateOfPurchase;

    @NotBlank
    @Column(name = "Publication Date")
    private Date publicationDate;
}
