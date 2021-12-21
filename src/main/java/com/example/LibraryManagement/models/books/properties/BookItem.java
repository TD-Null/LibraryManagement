package com.example.LibraryManagement.models.books.properties;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * Any book can have multiple copies, each copy will be considered
 * a book item in our system. Each book item will have a unique barcode.
 *
 * Many BookItem objects can be associated with a single library as well
 * as be associated to different or the same authors.
 */
@Data
@Entity
@Table
public class BookItem extends Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Barcode")
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "library_name", nullable = false)
    private Library library;

    @ManyToOne
    @JoinColumn(name = "rack_id")
    private Rack rack;

    @ManyToOne
    @JoinColumn(name = "author_name", nullable = false)
    private Author author;

    @Column(name = "Format", nullable = false)
    private BookFormat format;

    @Column(name = "Status", nullable = false)
    private BookStatus status;

    @Column(name = "Publication_Date", nullable = false)
    private Date publicationDate;

    @Column(name = "Reference")
    private boolean isReferenceOnly;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member currMember;

    @JsonIgnore
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookLending> lendingRecords = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookReservation> reservations = new HashSet<>();

    @Column(name = "Borrowed_Date")
    private Date borrowed;

    @Column(name = "Due_Date")
    private Date dueDate;

    @Column(name = "Price")
    private double price;

    @Column(name = "Date_of_Purchase")
    private Date dateOfPurchase;
}
