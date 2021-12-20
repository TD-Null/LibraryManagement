package com.example.LibraryManagement.models.books.libraries;

import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.datatypes.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * The central part of the organization for which this software has been designed.
 * It has attributes like ‘Name’ to distinguish it from any other libraries and
 * ‘Address’ to describe its location.
 *
 * Each library will contain its own books as well as racks to contain the books
 * for their locations within the library.
 *
 * Librarians can also modify libraries to add book items and additional racks
 * to a given library.
 */
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
    @OneToMany(mappedBy = "library", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookItem> books = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "library", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Rack> racks = new HashSet<>();

    public void addBookItem(BookItem b) { books.add(b); }

    public void addRack(Rack r) { racks.add(r); }

    public boolean removeBookItem(BookItem b) { return books.remove(b); }

    public boolean removeRack(Rack r) { return racks.remove(r); }
}
