package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.interfaces.MemberActions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * All members can search the catalog, as well as check-out, reserve, renew,
 * and return a book.
 *
 * Each member will have
 *
 * - A number keeping in track of how many books they have checked out or
 * reserved as they are allowed at most 5 books issued to their account.
 *
 * - Lists of records of books loaned or reserved for them in their account.
 *
 * - List of notifications that can be from book loans, reservations,
 * fines, or miscellaneous notifications that can be sent from librarians.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
public class Member extends Account implements MemberActions
{
    @NotBlank
    @Column(name = "Date_of_Membership")
    private Date dateOfMembership;

    @Column(name = "Issued_Books_Total")
    private int issuedBooksTotal;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookItem> checkedOutBooks = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookItem> reservedBooks = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookLending> bookLoans = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookReservation> bookReservations = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AccountNotification> notifications = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Fine> fines = new HashSet<>();

    // TODO: Add functionality for checking out a BookItem.
    public boolean checkOutBookItem(BookItem b) { return true; }

    // TODO: Add functionality renewing a BookItem.
    public boolean renewBookItem(BookItem b, BookLending bl) { return true; }

    // TODO: Add functionality for returning a BookItem.
    public boolean returnBookItem(BookItem b, BookLending bl) { return true; }

    // TODO: Add functionality reserving a BookItem.
    public boolean reserveBookItem(BookItem b) { return true; }

    // TODO: Add functionality for cancelling a reservation for a BookItem.
    public boolean cancelReservation(BookItem b, BookReservation br) { return true; }

    // TODO: Add functionality for checking out a reserved BookItem.
    public boolean checkOutReservedBookItem(BookItem b, BookReservation br) { return true; }
}
