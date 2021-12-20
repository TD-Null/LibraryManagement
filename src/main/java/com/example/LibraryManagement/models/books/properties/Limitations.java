package com.example.LibraryManagement.models.books.properties;

/*
 * Contains the limitations for a member to check out and reserve
 * books.
 *
 * There should be a maximum limit (5) on how many books a member
 * can check-out and reserve, and there should be a maximum limit
 * (10) on how many days a member can keep a book.
 */
public class Limitations
{
    public static final int MAX_ISSUED_BOOKS = 5;
    public static final int MAX_LENDING_DAYS = 10;
}
