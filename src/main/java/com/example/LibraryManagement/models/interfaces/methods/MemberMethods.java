package com.example.LibraryManagement.models.interfaces.methods;

import com.example.LibraryManagement.models.books.properties.BookItem;

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
    public void addBookItem(BookItem b);

    public void removeBookItem(BookItem b);

    public void reserveBookItem(BookItem b);

    public void removeReservedBookItem(BookItem b);

    public void addReservedBookItem(BookItem b);
}
