package com.example.LibraryManagement.models.books.properties;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = "books")
@NoArgsConstructor
@Entity
@Table
public class Subject
{
    @Id
    @NotBlank
    @Column(name = "Name", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private Set<BookItem> books = new HashSet<>();

    public Subject(String name) { this.name = name; }

    public void addBookItem(BookItem b) { books.add(b); }

    public void removeBookItem(BookItem b) { books.remove(b); }

    public void clearBooks() { books.clear(); }
}
