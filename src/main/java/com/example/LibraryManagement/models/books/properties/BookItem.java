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
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * Any book can have multiple copies, each copy will be considered
 * a book item in our system. Each book item will have a unique barcode.
 *
 * Many BookItem objects can be associated with a single library and be
 * placed on different racks as well as be associated to different or the
 * same authors.
 */
@Data
@EqualsAndHashCode(callSuper = true)
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

    @ManyToOne
    @JoinColumn(name = "subject_name", nullable = false)
    private Set<Subject> subjects = new HashSet<>();

    @Column(name = "Format", nullable = false)
    private BookFormat format;

    @Column(name = "Status", nullable = false)
    private BookStatus status;

    @Column(name = "Publication_Date", nullable = false)
    private Date publicationDate;

    @Column(name = "Reference")
    private boolean isReferenceOnly;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member currLoanMember;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member currReservedMember;

    @JsonIgnore
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookLending> lendingRecords = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookReservation> reservationRecords = new HashSet<>();

    @Column(name = "Borrowed_Date")
    private Date borrowed;

    @Column(name = "Due_Date")
    private Date dueDate;

    @Column(name = "Price")
    private double price;

    @Temporal(TemporalType.DATE)
    @Column(name = "Date_of_Purchase")
    private Date dateOfPurchase;
}
