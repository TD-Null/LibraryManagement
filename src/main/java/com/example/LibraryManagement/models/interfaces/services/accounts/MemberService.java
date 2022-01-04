package com.example.LibraryManagement.models.interfaces.services.accounts;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.fines.FineTransaction;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.sql.ResultSet;
import java.util.List;

// Methods used in a service component relating to members.
public interface MemberService
{
    ResponseEntity<BookItem> checkoutBook(Member member, BookItem book);

    ResponseEntity<MessageResponse> returnBook(Member member, BookItem book);

    ResponseEntity<MessageResponse> reserveBook(Member member, BookItem book);

    ResponseEntity<MessageResponse> cancelReservation(Member member, BookItem book);

    ResponseEntity<MessageResponse> renewBook(Member member, BookItem book);

    ResponseEntity<MessageResponse> payFine(Member member, Long fineID);
}
