package com.example.LibraryManagement.models.books.properties;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Author
{
    @NotBlank
    private String name;
    private String description;
}
