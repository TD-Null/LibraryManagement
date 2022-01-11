package com.example.LibraryManagement.models.interfaces.methods;

import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.BookItem;

import java.util.Date;

/*
 * Methods for the Member.class:
 *
 * - Adding a book item
 * - Removing a book item
 * - Reserving a book item
 * - Removing a reserved book item
 * - Adding a reserved book item
 */
public interface MemberMethods
{
    void checkoutBookItem(BookItem b, BookLending bl);

    void returnBookItem(BookItem b, Date returnDate);

    void reserveBookItem(BookItem b, BookReservation br);

    void checkoutReservedBookItem(BookItem b, BookLending bl);

    void updatedPendingReservation(BookItem b);

    void cancelReservedBookItem(BookItem b);

    void renewBookItem(BookItem b, Date dueDate, Date newDueDate);

    void sendNotification(AccountNotification notification);

    void addFine(Fine f);

    void payFine();
}
