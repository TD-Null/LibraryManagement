package com.example.LibraryManagement.models.books.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@EqualsAndHashCode
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

    public Author(String name) { this.name = name; }

    public Author(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public void addBookItem(BookItem b) { books.add(b); }

    public void removeBookItem(BookItem b) { books.remove(b); }
}
