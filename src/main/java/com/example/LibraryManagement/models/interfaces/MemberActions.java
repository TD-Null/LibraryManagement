package com.example.LibraryManagement.models.interfaces;

import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.properties.BookItem;

/*
 * Actions that a member account can do, including:
 *
 * - Checking out a book
 *
 * - Renewing a borrowed book's due date
 *
 * - Returning a borrowed book
 *
 * - Creating a reservation for a book
 *
 * - Cancelling a reservation for a book
 */
public interface MemberActions
{
    public boolean checkOutBookItem(BookItem b);

    public boolean renewBookItem(BookItem b, BookLending bl);

    public boolean returnBookItem(BookItem b, BookLending bl);

    public boolean reserveBookItem(BookItem b);

    public boolean cancelReservation(BookItem b, BookReservation br);

    public boolean checkOutReservedBookItem(BookItem b, BookReservation br);
}
