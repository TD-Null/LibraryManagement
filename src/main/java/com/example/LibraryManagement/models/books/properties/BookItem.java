package com.example.LibraryManagement.models.books.properties;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * Any book can have multiple copies, each copy will be considered
 * a book item in our system. Each book item will have a unique barcode.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"lendingRecords", "reservationRecords"})
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

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne
    @JoinColumn(name = "author_name")
    private Author author;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "subjects",
            joinColumns = @JoinColumn(name = "name"),
            inverseJoinColumns = @JoinColumn(name = "barcode"))
    private Set<Subject> subjects = new HashSet<>();

    @Column(name = "Format", nullable = false)
    private BookFormat format;

    @JsonIgnore
    @Column(name = "Status", nullable = false)
    private BookStatus status;

    @Temporal(TemporalType.DATE)
    @Column(name = "Publication", nullable = false)
    private Date publicationDate;

    @JsonIgnore
    @Column(name = "Reference")
    private boolean isReferenceOnly;

    @JsonIgnore
    @Column(name = "BorrowedDate")
    private Date borrowed;

    @JsonIgnore
    @Temporal(TemporalType.DATE)
    @Column(name = "DueDate")
    private Date dueDate;

    @Column(name = "Price")
    private double price;

    @JsonIgnore
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
                    String language, int numberOfPages, Rack rack,
                    BookFormat format, BookStatus status,
                    Date publicationDate, boolean isReferenceOnly,
                    double price)
    {
        super(ISBN, title, publisher, language, numberOfPages);
        setRack(rack);
        this.format = format;
        this.status = status;
        this.publicationDate = publicationDate;
        this.isReferenceOnly = isReferenceOnly;
        this.price = price;
    }

    public Rack getRack() { return new Rack(rackNumber, locationIdentifier); }

    public void setRack(Rack r)
    {
        rackNumber = r.getNumber();
        locationIdentifier = r.getLocation();
    }

    public void addSubject(Subject s) { subjects.add(s); }

    public void removeSubject(Subject s) { subjects.remove(s); }

    public void clearSubjects() { subjects.clear(); }

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
