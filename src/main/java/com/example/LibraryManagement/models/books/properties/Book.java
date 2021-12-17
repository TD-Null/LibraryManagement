package com.example.LibraryManagement.models.books.properties;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table
public class Book extends BookItem
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

    @NotBlank
    @Column(name = "Number of Pages")
    private int numberOfPages;
}
