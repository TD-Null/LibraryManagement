package com.example.LibraryManagement.models.io.requests.librarian_requests.post;

import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.responses.ValidationMessages;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
public class AddBookItemRequest extends CardValidationRequest
{
    @NotBlank(message = ValidationMessages.libraryMsg)
    private final String libraryName;

    private final int rack;

    private final String location;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Date publicationDate;

    private final boolean isReferenceOnly;

    private final double price;

    public AddBookItemRequest(Long barcode, String number, String libraryName, int rack, String location,
                              String ISBN, String title, String publisher, String language, int numberOfPages,
                              String authorName, Set<String> subjectNames, BookFormat format, Date publicationDate,
                              boolean isReferenceOnly, double price)
    {
        super(barcode, number);
        this.libraryName = libraryName;
        this.rack = rack;
        this.location = location;
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
