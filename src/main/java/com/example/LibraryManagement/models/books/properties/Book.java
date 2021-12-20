package com.example.LibraryManagement.models.books.properties;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@Data
@MappedSuperclass
public class Book
{
    @NotBlank
    @Column(name = "ISBN")
    private String ISBN;

    @NotBlank
    @Column(name = "Title")
    private String title;

    @NotBlank
    @Column(name = "Subject")
    private String subject;

    @NotBlank
    @Column(name = "Publisher")
    private String publisher;

    @NotBlank
    @Column(name = "Language")
    private String language;

    @Column(name = "Number of Pages")
    private int numberOfPages;
}
