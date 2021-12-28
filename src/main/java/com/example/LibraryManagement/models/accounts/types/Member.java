package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.interfaces.methods.MemberMethods;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * All members can search the catalog, as well as check-out, reserve, renew,
 * and return a book.
 *
 * Each member will have
 *
 * - A number keeping in track of how many books they have checked out or
 * reserved as they are allowed at most 5 books issued to their account.
 *
 * - List of records of books loaned or reserved for them in their account.
 *
 * - List of notifications that can be from book loans, reservations,
 * fines, or miscellaneous notifications that can be sent from librarians.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "Email")
})
public class Member extends Account implements MemberMethods
{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "barcode", nullable = false)
    private LibraryCard libraryCard;

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

    public Member(String name, String password, AccountStatus status,
                  Address address, String email, String phoneNumber,
                  Date dateOfMembership)
    {
        super(name, password, status, address, email, phoneNumber);
        this.dateOfMembership = dateOfMembership;
    }

    public void addBookItem(BookItem b)
    {
        checkedOutBooks.add(b);
        issuedBooksTotal++;
    }

    public void removeBookItem(BookItem b)
    {
        checkedOutBooks.remove(b);
        issuedBooksTotal--;
    }

    public void reserveBookItem(BookItem b)
    {
        reservedBooks.add(b);
        issuedBooksTotal++;
    }

    public void removeReservedBookItem(BookItem b)
    {
        reservedBooks.remove(b);
        issuedBooksTotal--;
    }

    public void addReservedBookItem(BookItem b)
    {
        removeBookItem(b);
        addBookItem(b);
    }
}
