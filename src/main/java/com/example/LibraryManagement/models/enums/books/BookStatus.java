package com.example.LibraryManagement.models.enums.books;

/*
 * Lists the statuses that a book can be labelled with.
 *
 * AVAILABLE: Indicates that the book is currently AVAILABLE
 * for borrowing from any user.
 *
 * RESERVED: Indicates that the book is currently RESERVED
 * for another user to borrow after the book has been returned.
 *
 * LOANED: Indicates that the book is currently unavailable
 * and has been LOANED to another user borrowing it.
 *
 * LOST: Indicates that the book is LOST and can not be obtained
 * in any manner.
 */
public enum BookStatus
{
    AVAILABLE,
    RESERVED,
    LOANED,
    LOST
}
