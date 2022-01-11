package com.example.LibraryManagement.models.enums.reservations;

/*
 * Lists the statuses that a book reservation can be labelled with.
 *
 * WAITING: This indicates that the user is currently WAITING for
 * the book to be available for borrowing after it has been returned
 * from another user.
 *
 * PENDING: This indicates that the book is currently available for
 * the user and is PENDING for the user to borrow the book from the
 * library.
 *
 * COMPLETED: This indicates that the book reservation has been
 * COMPLETED and the user was able to borrow the book after they have
 * reserved the book.
 *
 * CANCELLED: This indicates that the book reservation is CANCELLED
 * by the user and doesn't want to reserve the book for borrowing in
 * the future.
 *
 * NONE: Status used in the case unable to identify the under any of
 * the other statuses.
 */
public enum ReservationStatus
{
    WAITING,
    PENDING,
    COMPLETED,
    CANCELLED,
}
