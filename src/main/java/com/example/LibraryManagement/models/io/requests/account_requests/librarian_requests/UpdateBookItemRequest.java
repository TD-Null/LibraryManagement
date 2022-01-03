package com.example.LibraryManagement.models.io.requests.account_requests.librarian_requests;

import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.requests.account_requests.BarcodeValidationRequest;
import lombok.Getter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
public class UpdateBookItemRequest extends BarcodeValidationRequest
{
    @NotBlank
    private String ISBN;

    @NotBlank
    private String title;

    @NotBlank
    private String publisher;

    @NotBlank
    private String language;

    private int numberOfPages;

    @NotBlank
    private String authorName;

    @NotNull
    private Set<String> subjectNames;

    @NotNull
    private BookFormat format;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date publicationDate;

    private boolean isReferenceOnly;

    private double price;

    public UpdateBookItemRequest(String barcode, String ISBN, String title,
                                 String publisher, String language, int numberOfPages,
                                 String authorName, Set<String> subjectNames, BookFormat format,
                                 Date publicationDate, boolean isReferenceOnly, double price)
    {
        super(barcode);
        this.ISBN = ISBN;
        this.title = title;
        this.publisher = publisher;
        this.language = language;
        this.numberOfPages = numberOfPages;
        this.authorName = authorName;
        this.subjectNames = subjectNames;
        this.format = format;
        this.publicationDate = publicationDate;
        this.isReferenceOnly = isReferenceOnly;
        this.price = price;
    }
}