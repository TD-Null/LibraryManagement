package com.example.LibraryManagement.models.interfaces.services.accounts;

import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import org.springframework.http.ResponseEntity;

import java.util.List;

// Methods used in a service component relating to librarians.
public interface LibrarianService
{
    ResponseEntity<List<Librarian>> listAllLibrarians();

    ResponseEntity<List<Member>> listAllMembers();

    ResponseEntity<List<BookLending>> listAllBookLoans();

    ResponseEntity<List<BookReservation>> listAllBookReservations();

    ResponseEntity<List<Fine>> listAllFines();
}
