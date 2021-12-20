package com.example.LibraryManagement.models.books.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/*
 * Additional details for a Book/BookItem object regarding
 * its Authors, including their:
 *
 * Name
 * Description
 *
 * Each author can have multiple Book/BooKItem objects associated
 * to them.
 */
@Data
@Entity
@Table
public class Author
{
    @Id
    @NotBlank
    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookItem> books = new HashSet<>();
}
