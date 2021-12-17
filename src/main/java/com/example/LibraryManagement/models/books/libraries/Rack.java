package com.example.LibraryManagement.models.books.libraries;

import com.example.LibraryManagement.models.books.properties.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class Rack
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column
    private int number;
    private String locationIdentifier;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Library library;

    @JsonIgnore
    @OneToMany(mappedBy = "rack", cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();
}
