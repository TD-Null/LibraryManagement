package com.example.LibraryManagement.models.books.properties;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
@NoArgsConstructor
@Entity
@Table
public class BookItem extends Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Barcode")
    private Long barcode;

    @ManyToOne
    @JoinColumn(name = "library_name")
    private Library library;

    @Column(name = "Rack")
    private int rackNumber;

    @Column(name = "Location")
    private String locationIdentifier;

    @ManyToOne
    @JoinColumn(name = "author_name")
    private Author author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "subjects",
            joinColumns = @JoinColumn(name = "name"),
            inverseJoinColumns = @JoinColumn(name = "barcode"))
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
    @JoinColumn(name = "loan_member")
    private Member currLoanMember;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reserved_member")
    private Member currReservedMember;

    @JsonIgnore
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private Set<BookLending> lendingRecords = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bookItem", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private Set<BookReservation> reservationRecords = new HashSet<>();

    public BookItem(String ISBN, String title, String publisher,
                    String language, int numberOfPages, int rackNumber,
                    String locationIdentifier, BookFormat format, BookStatus status,
                    Date publicationDate, boolean isReferenceOnly, double price)
    {
        super(ISBN, title, publisher, language, numberOfPages);
        this.rackNumber = rackNumber;
        this.locationIdentifier = locationIdentifier;
        this.format = format;
        this.status = status;
        this.publicationDate = publicationDate;
        this.isReferenceOnly = isReferenceOnly;
        this.price = price;
    }

    public void setRack(Rack r)
    {
        rackNumber = r.getNumber();
        locationIdentifier = r.getLocation();
    }

    public void addSubject(Subject s) { subjects.add(s); }

    public void removeSubject(Subject s) { subjects.remove(s); }

    public void addLoanRecord(BookLending bl)
    {
        lendingRecords.add(bl);
    }

    public void addReservationRecord(BookReservation br) { reservationRecords.add(br); }

    public void clearRecords()
    {
        lendingRecords.clear();
        reservationRecords.clear();
    }
}
