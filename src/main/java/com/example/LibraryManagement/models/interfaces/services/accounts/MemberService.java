package com.example.LibraryManagement.models.interfaces.services.accounts;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.fines.FineTransaction;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.enums.fines.TransactionType;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

// Methods used in a service component relating to members.
public interface MemberService
{
    ResponseEntity<BookItem> checkoutBook(Member member, BookItem book, Date currDate);

    ResponseEntity<MessageResponse> returnBook(Member member, BookItem book, Date currDate);

    ResponseEntity<MessageResponse> reserveBook(Member member, BookItem book, Date currDate);

    ResponseEntity<MessageResponse> cancelReservation(Member member, BookItem book, Date currDate);

    ResponseEntity<MessageResponse> renewBook(Member member, BookItem book, Date currDate);

    ResponseEntity<MessageResponse> payFine(Member member, Fine fine, TransactionType type,
                                            Object Transaction, double amount, Date currDate);
}
