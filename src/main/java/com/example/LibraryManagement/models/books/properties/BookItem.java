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
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
@Getter
@Setter
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

    @ManyToMany
    @JoinTable(
            name = "subjects",
            joinColumns = @JoinColumn(name = "subject_name"),
            inverseJoinColumns = @JoinColumn(name = "book_barcode"))
    private Set<Subject> subjects = new HashSet<>();

    @Column(name = "Format", nullable = false)
    private BookFormat format;

    @Column(name = "Status", nullable = false)
    private BookStatus status;

    @Temporal(TemporalType.DATE)
    @Column(name = "Publication", nullable = false)
    private Date publicationDate;

    @Column(name = "Reference")
    private boolean isReferenceOnly;

    @Column(name = "BorrowedDate")
    private Date borrowed;

    @Temporal(TemporalType.DATE)
    @Column(name = "DueDate")
    private Date dueDate;

    @Column(name = "Price")
    private double price;

    @Temporal(TemporalType.DATE)
    @Column(name = "Purchase")
    private Date dateOfPurchase;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "loanMember")
    private Member currLoanMember;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reservedMember")
    private Member currReservedMember;

    @JsonIgnore
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookLending> lendingRecords = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookReservation> reservationRecords = new HashSet<>();

    public BookItem(String ISBN, String title, String publisher, String language,
                    int numberOfPages, BookFormat format, BookStatus status,
                    Date publicationDate, boolean isReferenceOnly, double price)
    {
        super(ISBN, title, publisher, language, numberOfPages);
        this.format = format;
        this.status = status;
        this.publicationDate = publicationDate;
        this.isReferenceOnly = isReferenceOnly;
        this.price = price;
    }

    public void addLoanRecord(BookLending bl)
    {
        lendingRecords.add(bl);
    }

    public void addReservationRecord(BookReservation br) { reservationRecords.add(br); }
}
