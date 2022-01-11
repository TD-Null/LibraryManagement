package com.example.LibraryManagement.models.io.requests.librarian_requests;

import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.requests.BarcodeValidationRequest;
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
    private final String ISBN;

    @NotBlank
    private final String title;

    @NotBlank
    private final String publisher;

    @NotBlank
    private final String language;

    private final int numberOfPages;

    @NotBlank
    private final String authorName;

    @NotNull
    private final Set<String> subjectNames;

    @NotNull
    private final BookFormat format;

    @NotNull
    @Temporal(TemporalType.DATE)
    private final Date publicationDate;

    private final boolean isReferenceOnly;

    private final double price;

    public UpdateBookItemRequest(Long barcode, String ISBN, String title,
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
