package com.example.LibraryManagement.models.books.libraries;

import com.example.LibraryManagement.models.books.properties.BookItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * Books can be placed on racks. Each rack will be identified by a rack number
 * and will have a location identifier to describe the physical location of the
 * rack in the library.
 *
 * Each rack is associated with a single library, some of which will associate
 * with the same library.
 *
 * Each rack can contain multiple BookItems of its library.
 */
@Data
@Entity
@Table
public class Rack
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Number")
    private int number;

    @Column(name = "Location")
    private String locationIdentifier;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "library_name", nullable = false)
    private Library library;

    @JsonIgnore
    @OneToMany(mappedBy = "rack", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookItem> books = new HashSet<>();
}
