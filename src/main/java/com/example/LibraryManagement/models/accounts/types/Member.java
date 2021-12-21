package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/*
 * Description:
 * All members can search the catalog, as well as check-out, reserve, renew,
 * and return a book.
 */
@Data
@Entity
@Table
public class Member extends Account
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
    private Set<BookLending> bookLendings = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookReservation> bookReservations = new HashSet<>();

    // TODO: Add functionality for checking out a BookItem.
    public boolean checkOutBookItem(BookItem b) { return true; }

    // TODO: Add functionality reserving a BookItem.
    public boolean renewBookItem(BookItem b) { return true; }

    // TODO: Add functionality for returning a BookItem.
    public boolean returnBookItem(BookItem b) { return true; }

    // TODO: Add functionality reserving a BookItem.
    public boolean reserveBookItem(BookItem b) { return true; }

    // TODO: Add functionality for cancelling a reservation for a BookItem.
    public boolean cancelReservation(BookItem b) { return true; }
}
