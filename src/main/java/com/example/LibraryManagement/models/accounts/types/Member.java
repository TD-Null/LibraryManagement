package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.fines.FineTransaction;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.reservations.ReservationStatus;
import com.example.LibraryManagement.models.interfaces.methods.MemberMethods;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class Member extends Account
{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card")
    private LibraryCard libraryCard;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "Date")
    private Date dateOfMembership;

    @Column(name = "IssuedBooks")
    private int issuedBooksTotal = 0;

    @Column(name = "Fines")
    private int totalFines = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "currLoanMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookItem> checkedOutBooks = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "currReservedMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    public void checkoutBookItem(BookItem b, BookLending bl)
    {
        checkedOutBooks.add(b);
        bookLoans.add(bl);
        issuedBooksTotal++;
    }

    public void returnBookItem(BookItem b, Date returnDate)
    {
        checkedOutBooks.remove(b);

        for(BookLending bl: bookLoans)
        {
            if(bl.getBookItem().equals(b) && bl.getDueDate().compareTo(b.getDueDate()) == 0)
            {
                bl.setReturnDate(returnDate);
                break;
            }
        }

        issuedBooksTotal--;
    }

    public void reserveBookItem(BookItem b, BookReservation br)
    {
        reservedBooks.add(b);
        bookReservations.add(br);
        issuedBooksTotal++;
    }

    public void checkoutReservedBookItem(BookItem b, BookLending bl)
    {
        reservedBooks.remove(b);

        for(BookReservation br: bookReservations)
        {
            if(br.getBookItem().equals(b) && br.getStatus() == ReservationStatus.PENDING)
            {
                br.setStatus(ReservationStatus.COMPLETED);
            }
        }

        issuedBooksTotal--;
        checkoutBookItem(b, bl);
    }

    public void updatedPendingReservation(BookItem b)
    {
        for(BookReservation br: bookReservations)
        {
            if(br.getBookItem().equals(b) && br.getStatus() == ReservationStatus.WAITING)
            {
                br.setStatus(ReservationStatus.PENDING);
            }
        }
    }

    public void cancelReservedBookItem(BookItem b)
    {
        reservedBooks.remove(b);

        for(BookReservation br: bookReservations)
        {
            if(br.getBookItem().equals(b) &&
                    (br.getStatus() == ReservationStatus.WAITING
                            || br.getStatus() == ReservationStatus.PENDING))
            {
                br.setStatus(ReservationStatus.CANCELLED);
            }
        }

        issuedBooksTotal--;
    }

    public void renewBookItem(BookItem b, Date dueDate, Date newDueDate)
    {
        for(BookLending bl: bookLoans)
        {
            if(bl.getBookItem().equals(b) && bl.getDueDate().compareTo(dueDate) == 0)
            {
                bl.setDueDate(newDueDate);
                break;
            }
        }
    }

    public void sendNotification(AccountNotification notification) { notifications.add(notification); }

    public void addFine(Fine f)
    {
        fines.add(f);
        totalFines++;
    }

    public void payFine() { totalFines--; }
}
