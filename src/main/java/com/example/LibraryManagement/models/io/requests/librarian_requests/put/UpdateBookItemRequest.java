package com.example.LibraryManagement.models.io.requests.librarian_requests.put;

import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import lombok.Getter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
public class UpdateBookItemRequest extends CardValidationRequest
{
    @NotNull(message = ValidationMessages.bookBarcodeMsg)
    private final Long bookBarcode;

    @NotBlank(message = ValidationMessages.bookISBNMsg)
    private final String isbn;

    @NotBlank(message = ValidationMessages.bookTitleMsg)
    private final String title;

    @NotBlank(message = ValidationMessages.bookPublisherMsg)
    private final String publisher;

    @NotBlank(message = ValidationMessages.bookLanguageMsg)
    private final String language;

    private final int numberOfPages;

    @NotBlank(message = ValidationMessages.authorMsg)
    private final String author;

    private final Set<String> subjectNames;

    @NotNull(message = ValidationMessages.bookFormatMsg)
    private final BookFormat format;

    @NotNull(message = ValidationMessages.dateFormatMsg)
    @Temporal(TemporalType.DATE)
    private final Date publicationDate;

    private final boolean isReferenceOnly;

    private final double price;

    public UpdateBookItemRequest(Long barcode, String number, Long bookBarcode,
                                 String ISBN, String title, String publisher,
                                 String language, int numberOfPages, String authorName,
                                 Set<String> subjectNames, BookFormat format, Date publicationDate,
                                 boolean isReferenceOnly, double price)
    {
        super(barcode, number);
        this.bookBarcode = bookBarcode;
        this.isbn = ISBN;
        this.title = title;
        this.publisher = publisher;
        this.language = language;
        this.numberOfPages = numberOfPages;
        this.author = authorName;
        this.subjectNames = subjectNames;
        this.format = format;
        this.publicationDate = publicationDate;
        this.isReferenceOnly = isReferenceOnly;
        this.price = price;
    }
}
