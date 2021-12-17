package com.example.LibraryManagement.models.books.libraries;

import com.example.LibraryManagement.models.books.properties.Book;
import com.example.LibraryManagement.models.datatypes.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class Library
{
    @Id
    @NotBlank
    @Column(name = "Name")
    private String name;

    @NotBlank
    @Column(name = "Address")
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private Set<Rack> racks = new HashSet<>();
}
