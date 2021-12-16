package com.example.LibraryManagement.models.books.properties;

import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookItem extends Book
{
    private String barcode;
    private boolean isReferenceOnly;
    private Date borrowed;
    private Date dueDate;
    private double price;
    private BookFormat format;
    private BookStatus status;
    private Date dateOfPurchase;
    private Date publicationDate;

    public BookItem(String ISBN, String title, String subject,
                    String publisher, String language, int numberOfPages,
                    String barcode, boolean isReferenceOnly, Date borrowed,
                    Date dueDate, double price, BookFormat format, BookStatus status,
                    Date dateOfPurchase, Date publicationDate)
    {
        super(ISBN, title, subject, publisher, language, numberOfPages);
        this.barcode = barcode;
        this.isReferenceOnly = isReferenceOnly;
        this.borrowed = borrowed;
        this.dueDate = dueDate;
        this.price = price;
        this.format = format;
        this.status = status;
        this.dateOfPurchase = dateOfPurchase;
        this.publicationDate = publicationDate;
    }

    // Method for checking out a book.
    public boolean checkout()
    {
        return true;
    }
}
