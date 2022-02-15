package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.components.services.accounts.MemberServiceImp;
import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.fines.FineTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CashTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CheckTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CreditCardTransaction;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.enums.fines.TransactionType;
import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.requests.member_requests.CancelMembershipRequest;
import com.example.LibraryManagement.models.io.requests.member_requests.CardTransactionRequest;
import com.example.LibraryManagement.models.io.requests.member_requests.CashTransactionRequest;
import com.example.LibraryManagement.models.io.requests.member_requests.CheckTransactionRequest;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Controller component containing the API requests relating to Members:
 *
 * - Viewing all books currently issued (loans and reservations)
 * - Viewing all account notifications
 * - Viewing all fines issued and transactions made
 * - Viewing all book checkout and reservation history made on the account
 * - Borrow, reserve, and renew books
 * - Cancel membership
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("library_website/account/member")
public class MemberController
{
    @Autowired
    private final AccountServiceImp accountService;
    @Autowired
    private final MemberServiceImp memberService;
    @Autowired
    private final ValidationService validationService;

    @GetMapping("/checkout/books")
    public ResponseEntity<List<BookItem>> viewBookLoans(HttpServletRequest httpServletRequest,
                                                        @RequestParam(value = "barcode") Long barcode,
                                                        @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_bookLoans = 0;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            List<BookItem> bookLoans = new ArrayList<>(member.getCheckedOutBooks());

            if (bookLoans.isEmpty())
                throw new ApiRequestException("Member has no books borrowed currently.", HttpStatus.NOT_FOUND);

            num_bookLoans = bookLoans.size();
            requestSuccess = true;
            return ResponseEntity.ok(bookLoans);
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Member has viewed their book loans. (# Borrowed Books:" + num_bookLoans + ")";

                else
                    message = "Member has no current book loans.";
            }

            else
                message = "Member was unable to obtain current book loans.";

            memberViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/reserve/books")
    public ResponseEntity<List<BookItem>> viewReservedBooks(HttpServletRequest httpServletRequest,
                                                            @RequestParam(value = "barcode") Long barcode,
                                                            @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_bookReservations = 0;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            List<BookItem> bookReservations = new ArrayList<>(member.getReservedBooks());

            if (bookReservations.isEmpty())
                throw new ApiRequestException("Member has no books reserved currently.", HttpStatus.NOT_FOUND);

            num_bookReservations = bookReservations.size();
            requestSuccess = true;
            return ResponseEntity.ok(bookReservations);
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Member has viewed their book reservations. (# Reserved Books:" + num_bookReservations + ")";

                else
                    message = "Member has no current book reservations.";
            }

            else
                message = "Member was unable to obtain current book reservations.";

            memberViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<AccountNotification>> viewNotifications(HttpServletRequest httpServletRequest,
                                                                       @RequestParam(value = "barcode") Long barcode,
                                                                       @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_notifications = 0;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            List<AccountNotification> notifications = new ArrayList<>(member.getNotifications());

            if (notifications.isEmpty())
                throw new ApiRequestException("Member has no notifications currently.", HttpStatus.NOT_FOUND);

            num_notifications = notifications.size();
            requestSuccess = true;
            return ResponseEntity.ok(notifications);
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Member has viewed their notifications. (# Notifications:" + num_notifications + ")";

                else
                    message = "Member has no current notifications.";
            }

            else
                message = "Member was unable to obtain current notifications.";

            memberViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/fines")
    public ResponseEntity<List<Fine>> viewFines(HttpServletRequest httpServletRequest,
                                                @RequestParam(value = "barcode") Long barcode,
                                                @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_fines = 0;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            List<Fine> fines = new ArrayList<>(member.getFines());

            if (fines.isEmpty())
                throw new ApiRequestException("Member has no fines currently.", HttpStatus.NOT_FOUND);

            num_fines = fines.size();
            requestSuccess = true;
            return ResponseEntity.ok(fines);
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Member has viewed their current fines. (# Fines:" + num_fines + ")";

                else
                    message = "Member has no current fines.";
            }

            else
                message = "Member was unable to obtain current fines.";

            memberViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<FineTransaction>> viewTransactions(HttpServletRequest httpServletRequest,
                                                                  @RequestParam(value = "barcode") Long barcode,
                                                                  @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_transactions = 0;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            List<FineTransaction> transactions = new ArrayList<>();
            List<Fine> fines = new ArrayList<>(member.getFines());

            for (Fine f : fines)
            {
                FineTransaction t = f.getFineTransaction();

                if (t != null) {
                    transactions.add(t);
                }
            }

            if (transactions.isEmpty())
                throw new ApiRequestException("Member has not made any transactions currently.", HttpStatus.NOT_FOUND);

            num_transactions = transactions.size();
            requestSuccess = true;
            return ResponseEntity.ok(transactions);
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Member has viewed their current transactions. (# Transactions:" + num_transactions + ")";

                else
                    message = "Member has no current transactions.";
            }

            else
                message = "Member was unable to obtain current transactions.";

            memberViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/checkout/history")
    public ResponseEntity<List<BookLending>> viewCheckoutHistory(HttpServletRequest httpServletRequest,
                                                                 @RequestParam(value = "barcode") Long barcode,
                                                                 @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_checkouts = 0;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(barcode, number);
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            List<BookLending> checkoutRecords = new ArrayList<>(member.getBookLoans());

            if (checkoutRecords.isEmpty())
                throw new ApiRequestException("Member has no history of books checked out.", HttpStatus.NOT_FOUND);

            num_checkouts = checkoutRecords.size();
            requestSuccess = true;
            return ResponseEntity.ok(checkoutRecords);
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Member has viewed their book checkout history. (# Checkouts:" + num_checkouts + ")";

                else
                    message = "Member has no current history of book checkouts.";
            }

            else
                message = "Member was unable to obtain current history of book checkouts.";

            memberViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/reserve/history")
    public ResponseEntity<List<BookReservation>> viewReservationHistory(HttpServletRequest httpServletRequest,
                                                                        @RequestParam(value = "barcode") Long barcode,
                                                                        @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_reservations = 0;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            List<BookReservation> reservationRecords = new ArrayList<>(member.getBookReservations());

            if (reservationRecords.isEmpty())
                throw new ApiRequestException("Member has no history of book reservations.", HttpStatus.NOT_FOUND);

            num_reservations = reservationRecords.size();
            requestSuccess = true;
            return ResponseEntity.ok(reservationRecords);
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Member has viewed their book reservations history. (# Reservations:" + num_reservations + ")";

                else
                    message = "Member has no current history of book reservations.";
            }

            else
                message = "Member was unable to obtain current history of book reservations.";

            memberViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @PutMapping("/checkout")
    public ResponseEntity<BookItem> checkoutBook(HttpServletRequest httpServletRequest,
                                                 @Valid @RequestBody CardValidationRequest request,
                                                 @RequestParam(value = "book") Long bookBarcode)
    {
        boolean cardValidationSuccess = false;
        boolean bookValidationSuccess = false;
        boolean requestSuccess = false;
        String bookTitle = "";
        ResponseEntity<BookItem> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            BookItem book = validationService.bookValidation(bookBarcode);
            bookValidationSuccess = true;
            bookTitle = book.getTitle();

            response = memberService.checkoutBook(member, book, new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has borrowed the book \"" + bookTitle + "\".";

            else
                message = "Member was unable to borrow a book from the system.";

            memberBookRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, request.getBarcode(), request.getNumber(), bookBarcode,
                    cardValidationSuccess, bookValidationSuccess, requestSuccess,
                    time);
        }
    }

    @PutMapping("/return")
    public ResponseEntity<MessageResponse> returnBook(HttpServletRequest httpServletRequest,
                                                      @Valid @RequestBody CardValidationRequest request,
                                                      @RequestParam(value = "book") Long bookBarcode)
    {
        boolean cardValidationSuccess = false;
        boolean bookValidationSuccess = false;
        boolean requestSuccess = false;
        String bookTitle = "";
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            BookItem book = validationService.bookValidation(bookBarcode);
            bookValidationSuccess = true;
            bookTitle = book.getTitle();

            response = memberService.returnBook(member, book, new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has returned the book \"" + bookTitle + "\".";

            else
                message = "Member has either returned a borrowed book late " +
                        "or was unable to return a book from the system.";

            memberBookRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, request.getBarcode(), request.getNumber(), bookBarcode,
                    cardValidationSuccess, bookValidationSuccess, requestSuccess,
                    time);
        }
    }

    @PutMapping("/reserve")
    public ResponseEntity<MessageResponse> reserveBook(HttpServletRequest httpServletRequest,
                                                       @Valid @RequestBody CardValidationRequest request,
                                                       @RequestParam(value = "book") Long bookBarcode)
    {
        boolean cardValidationSuccess = false;
        boolean bookValidationSuccess = false;
        boolean requestSuccess = false;
        String bookTitle = "";
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            BookItem book = validationService.bookValidation(bookBarcode);
            bookValidationSuccess = true;
            bookTitle = book.getTitle();

            response = memberService.reserveBook(member, book, new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has reserved the book \"" + bookTitle + "\".";

            else
                message = "Member was unable to reserve a book from the system.";

            memberBookRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, request.getBarcode(), request.getNumber(), bookBarcode,
                    cardValidationSuccess, bookValidationSuccess, requestSuccess,
                    time);
        }
    }

    @PutMapping("/reserve/cancel")
    public ResponseEntity<MessageResponse> cancelReservation(HttpServletRequest httpServletRequest,
                                                             @Valid @RequestBody CardValidationRequest request,
                                                             @RequestParam(value = "book") Long bookBarcode)
    {
        boolean cardValidationSuccess = false;
        boolean bookValidationSuccess = false;
        boolean requestSuccess = false;
        String bookTitle = "";
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            BookItem book = validationService.bookValidation(bookBarcode);
            bookValidationSuccess = true;
            bookTitle = book.getTitle();

            response = memberService.cancelReservation(member, book, new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has cancelled their reservation of the book \"" + bookTitle + "\".";

            else
                message = "Member was unable to cancel a reservation.";

            memberBookRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, request.getBarcode(), request.getNumber(), bookBarcode,
                    cardValidationSuccess, bookValidationSuccess, requestSuccess,
                    time);
        }
    }

    @PutMapping("/renew")
    public ResponseEntity<MessageResponse> renewBook(HttpServletRequest httpServletRequest,
                                                     @Valid @RequestBody CardValidationRequest request,
                                                     @RequestParam(value = "book") Long bookBarcode)
    {
        boolean cardValidationSuccess = false;
        boolean bookValidationSuccess = false;
        boolean requestSuccess = false;
        String bookTitle = "";
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            BookItem book = validationService.bookValidation(bookBarcode);
            bookValidationSuccess = true;
            bookTitle = book.getTitle();

            response = memberService.renewBook(member, book, new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has renewed their borrowed book \"" + bookTitle + "\".";

            else
                message = "Member was unable to renew one of their borrowed books from the system. " +
                        "(Either returned late or book is not associated with the member)";

            memberBookRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, request.getBarcode(), request.getNumber(), bookBarcode,
                    cardValidationSuccess, bookValidationSuccess, requestSuccess,
                    time);
        }
    }

    @PutMapping("/fines/transaction/card")
    public ResponseEntity<MessageResponse> cardTransaction(HttpServletRequest httpServletRequest,
                                                           @Valid @RequestBody CardTransactionRequest request,
                                                           @RequestParam(value = "fine") Long fineID)
    {
        boolean cardValidationSuccess = false;
        boolean fineValidationSuccess = false;
        boolean requestSuccess = false;
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            Fine fine = validationService.fineValidation(fineID);
            fineValidationSuccess = true;

            response =  memberService.payFine(member, fine, TransactionType.CREDIT_CARD,
                    new CreditCardTransaction(request.getName()), request.getAmount(),
                    new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has paid for their fine of + $" + request.getAmount() + ".";

            else
                message = "Member was unable to pay for their fine.";

            memberTransactionRequestLog(httpServletRequest.getRequestURL().toString(), message,
                    request.getBarcode(), request.getNumber(), fineID, TransactionType.CREDIT_CARD,
                    request.getAmount(), cardValidationSuccess, fineValidationSuccess,
                    requestSuccess, time);
        }
    }

    @PutMapping("/fines/transaction/check")
    public ResponseEntity<MessageResponse> checkTransaction(HttpServletRequest httpServletRequest,
                                                            @Valid @RequestBody CheckTransactionRequest request,
                                                            @RequestParam(value = "fine") Long fineID)
    {
        boolean cardValidationSuccess = false;
        boolean fineValidationSuccess = false;
        boolean requestSuccess = false;
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            Fine fine = validationService.fineValidation(fineID);
            fineValidationSuccess = true;

            response = memberService.payFine(member, fine, TransactionType.CHECK,
                    new CheckTransaction(request.getBankName(), request.getCheckNumber()), request.getAmount(),
                    new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has paid for their fine of + $" + request.getAmount() + ".";

            else
                message = "Member was unable to pay for their fine.";

            memberTransactionRequestLog(httpServletRequest.getRequestURL().toString(), message,
                    request.getBarcode(), request.getNumber(), fineID, TransactionType.CHECK,
                    request.getAmount(), cardValidationSuccess, fineValidationSuccess,
                    requestSuccess, time);
        }
    }

    @PutMapping("/fines/transaction/cash")
    public ResponseEntity<MessageResponse> cashTransaction(HttpServletRequest httpServletRequest,
                                                           @Valid @RequestBody CashTransactionRequest request,
                                                           @RequestParam(value = "fine") Long fineID)
    {
        boolean cardValidationSuccess = false;
        boolean fineValidationSuccess = false;
        boolean requestSuccess = false;
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            Member member = (Member) accountService.barcodeReader(
                    card, AccountType.MEMBER, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            Fine fine = validationService.fineValidation(fineID);
            fineValidationSuccess = true;

            response = memberService.payFine(member, fine, TransactionType.CASH,
                    new CashTransaction(request.getCashTendered()), request.getCashTendered(),
                    new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has paid for their fine of + $" + request.getCashTendered() + ".";

            else
                message = "Member was unable to pay for their fine.";

            memberTransactionRequestLog(httpServletRequest.getRequestURL().toString(), message,
                    request.getBarcode(), request.getNumber(), fineID, TransactionType.CASH,
                    request.getCashTendered(), cardValidationSuccess, fineValidationSuccess,
                    requestSuccess, time);
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<MessageResponse> cancelMembership(HttpServletRequest httpServletRequest,
                                                            @Valid @RequestBody CancelMembershipRequest request)
    {
        boolean requestSuccess = false;
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard libraryCard = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            response = accountService.cancelMemberAccount(libraryCard, request.getPassword());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if (requestSuccess)
                message = "Member has cancelled their membership.";

            else
                message = "Member was unable to cancel their membership.";

            memberCancelRequestLog(httpServletRequest.getRequestURL().toString(), message,
                    request.getBarcode(), request.getNumber(), request.getPassword(),
                    requestSuccess, time);
        }
    }

    private void memberViewRequestLog(String requestURL, String message, long barcode, String number,
                                      boolean cardValidation, boolean requestSuccess, long time)
    {
        String requestType = "GET";
        String userLog = "(Member:" +
                " Card Barcode = " + barcode +
                ", Card Number = " + number;
        String successLog = "(Success! Completed in " + time + " ms)";

        if(cardValidation)
            userLog += " [Valid])";

        else
            userLog += " [Invalid])";

        if(requestSuccess)
            successLog = "(Success! Completed in " + time + " ms. Date: " + new Date() + ")";

        else
            successLog = "(Failure! Completed in " + time + " ms. Date: " + new Date() + ")";

        log.info(requestType + " " + requestURL + " " + message + " " +
                userLog + " " + successLog);
    }

    private void memberBookRequestLog(String requestURL, String message, long barcode, String number,
                                      Long bookBarcode, boolean cardValidation, boolean bookValidation,
                                      boolean requestSuccess, long time)
    {
        String requestType = "PUT";
        String userLog = "(Member:" +
                " Card Barcode = " + barcode +
                ", Card Number = " + number;
        String bookLog = "(Book: " +
                "Barcode = " + bookBarcode;
        String successLog;

        if(cardValidation)
            userLog += " [Valid])";

        else
            userLog += " [Invalid])";

        if(bookValidation)
            bookLog += " [Valid])";

        else
            bookLog += " [Invalid])";

        if(requestSuccess)
            successLog = "(Success! Completed in " + time + " ms. Date: " + new Date() + ")";

        else
            successLog = "(Failure! Completed in " + time + " ms. Date: " + new Date() + ")";

        log.info(requestType + " " + requestURL + " " + message + " " +
                userLog + " " + bookLog + " " + successLog);
    }

    private void memberTransactionRequestLog(String requestURL, String message, long barcode, String number,
                                             Long fineID, TransactionType transactionType, double amount,
                                             boolean cardValidation, boolean fineValidation,
                                             boolean requestSuccess, long time)
    {
        String requestType = "PUT";
        String userLog = "(Member:" +
                " Card Barcode = " + barcode +
                ", Card Number = " + number;

        String fineLog = "(Fine: ID = " + fineID;

        String transactionLog = "(Transaction:" +
                " Amount = " + amount;

        if(transactionType == TransactionType.CREDIT_CARD)
            transactionLog += " Type = Credit Card";

        else if(transactionType == TransactionType.CHECK)
            transactionLog += " Type = Check";

        else if(transactionType == TransactionType.CASH)
            transactionLog += " Type = Cash";

        String successLog;

        if(cardValidation)
            userLog += " [Valid])";

        else
            userLog += " [Invalid])";

        if(fineValidation)
            fineLog += " [Valid])";

        else
            fineLog += " [Invalid])";

        if(requestSuccess)
        {
            transactionLog += " [Valid])";
            successLog = "(Success! Completed in " + time + " ms. Date: " + new Date() + ")";
        }

        else
        {
            transactionLog += " [Invalid])";
            successLog = "(Failure! Completed in " + time + " ms. Date: " + new Date() + ")";
        }

        log.info(requestType + " " + requestURL + " " + message + " " +
                userLog + " " + fineLog + " " + transactionLog + " " +
                successLog);
    }

    private void memberCancelRequestLog(String requestURL, String message, long barcode, String number,
                                        String password, boolean requestSuccess, long time)
    {
        String requestType = "DELETE";
        String userLog = "(Cancelled Member:" +
                " Card Barcode = " + barcode +
                ", Card Number = " + number +
                ", Password = " + password;
        String successLog;

        if(requestSuccess)
        {
            userLog += " [Valid])";
            successLog = "(Success! Completed in " + time + " ms. Date: " + new Date() + ")";
        }

        else
        {
            userLog += " [Invalid])";
            successLog = "(Failure! Completed in " + time + " ms. Date: " + new Date() + ")";
        }

        log.info(requestType + " " + requestURL + " " + message + " " +
                userLog + " " + successLog);
    }
}
