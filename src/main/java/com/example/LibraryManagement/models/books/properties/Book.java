package com.example.LibraryManagement.models.books.properties;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

/*
 * Description:
 * The basic building block of the system. Every book will
 * have ISBN, Title, Subject, Publishers, etc.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "Publisher")
    private String publisher;

    @NotBlank
    @Column(name = "Language")
    private String language;

    @Column(name = "Number_of_Pages")
    private int numberOfPages;
}
