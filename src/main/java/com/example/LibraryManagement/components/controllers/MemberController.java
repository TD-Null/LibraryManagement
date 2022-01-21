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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<List<BookItem>> viewBooksLoans(@RequestParam(value = "id") Long barcode,
                                                         @RequestParam(value = "card") String number)
    {
        Member member = (Member) accountService.barcodeReader(barcode, number,
                AccountType.MEMBER, AccountStatus.ACTIVE);
        List<BookItem> bookLoans = new ArrayList<>(member.getCheckedOutBooks());

        if(bookLoans.isEmpty())
            throw new ApiRequestException("Member has no books borrowed currently.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(bookLoans);
    }

    @GetMapping("/reserve/books")
    public ResponseEntity<List<BookItem>> viewReservedBooks(@RequestParam(value = "id") Long barcode,
                                                            @RequestParam(value = "card") String number)
    {
        Member member = (Member) accountService.barcodeReader(barcode, number,
                AccountType.MEMBER, AccountStatus.ACTIVE);
        List<BookItem> bookReservations = new ArrayList<>(member.getReservedBooks());

        if(bookReservations.isEmpty())
            throw new ApiRequestException("Member has no books reserved currently.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(bookReservations);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<AccountNotification>> viewNotifications(@RequestParam(value = "id") Long barcode,
                                                                       @RequestParam(value = "card") String number)
    {
        Member member = (Member) accountService.barcodeReader(barcode, number,
                AccountType.MEMBER, AccountStatus.ACTIVE);
        List<AccountNotification> notifications = new ArrayList<>(member.getNotifications());

        if(notifications.isEmpty())
            throw new ApiRequestException("Member has no notifications currently.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/fines")
    public ResponseEntity<List<Fine>> viewFines(@RequestParam(value = "id") Long barcode,
                                                @RequestParam(value = "card") String number)
    {
        Member member = (Member) accountService.barcodeReader(barcode, number,
                AccountType.MEMBER, AccountStatus.ACTIVE);
        List<Fine> fines = new ArrayList<>(member.getFines());

        if(fines.isEmpty())
            throw new ApiRequestException("Member has no fines currently.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(fines);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<FineTransaction>> viewTransactions(@RequestParam(value = "id") Long barcode,
                                                                  @RequestParam(value = "card") String number)
    {
        Member member = (Member) accountService.barcodeReader(barcode, number,
                AccountType.MEMBER, AccountStatus.ACTIVE);
        List<FineTransaction> transactions = new ArrayList<>();
        List<Fine> fines = new ArrayList<>(member.getFines());

        for(Fine f: fines)
        {
            FineTransaction t = f.getFineTransaction();

            if(t != null)
            {
                transactions.add(t);
            }
        }

        if(transactions.isEmpty())
            throw new ApiRequestException("Member has not made any transactions currently.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/checkout/history")
    public ResponseEntity<List<BookLending>> viewCheckoutHistory(@RequestParam(value = "barcode") Long barcode,
                                                                 @RequestParam(value = "card") String number)
    {
        Member member = (Member) accountService.barcodeReader(barcode, number,
                AccountType.MEMBER, AccountStatus.ACTIVE);
        List<BookLending> checkoutRecords = new ArrayList<>(member.getBookLoans());

        if(checkoutRecords.isEmpty())
            throw new ApiRequestException("Member has no history of books checked out.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(checkoutRecords);
    }

    @GetMapping("/reserve/history")
    public ResponseEntity<List<BookReservation>> viewReservationHistory(@RequestParam(value = "barcode") Long barcode,
                                                                        @RequestParam(value = "card") String number)
    {
        Member member = (Member) accountService.barcodeReader(barcode, number,
                AccountType.MEMBER, AccountStatus.ACTIVE);
        List<BookReservation> reservationRecords = new ArrayList<>(member.getBookReservations());

        if(reservationRecords.isEmpty())
            throw new ApiRequestException("Member has no history of book reservations.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(reservationRecords);
    }

    @PutMapping("/checkout")
    public ResponseEntity<BookItem> checkoutBook(@Valid @RequestBody CardValidationRequest request,
                                                 @RequestParam(value = "book") Long bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = validationService.bookValidation(bookBarcode);
        return memberService.checkoutBook(member, book, new Date());
    }

    @PutMapping("/return")
    public ResponseEntity<MessageResponse> returnBook(@Valid @RequestBody CardValidationRequest request,
                                                      @RequestParam(value = "book") Long bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = validationService.bookValidation(bookBarcode);
        return memberService.returnBook(member, book, new Date());
    }

    @PutMapping("/reserve")
    public ResponseEntity<MessageResponse> reserveBook(@Valid @RequestBody CardValidationRequest request,
                                                       @RequestParam(value = "book") Long bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = validationService.bookValidation(bookBarcode);
        return memberService.reserveBook(member, book, new Date());
    }

    @PutMapping("/reserve/cancel")
    public ResponseEntity<MessageResponse> cancelReservation(@Valid @RequestBody CardValidationRequest request,
                                                             @RequestParam(value = "book") Long bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = validationService.bookValidation(bookBarcode);
        return memberService.cancelReservation(member, book, new Date());
    }

    @PutMapping("/renew")
    public ResponseEntity<MessageResponse> renewBook(@Valid @RequestBody CardValidationRequest request,
                                                     @RequestParam(value = "book") Long bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = validationService.bookValidation(bookBarcode);
        return memberService.renewBook(member, book, new Date());
    }

    @PutMapping("/fines/transaction/card")
    public ResponseEntity<MessageResponse> cardTransaction(@Valid @RequestBody CardTransactionRequest request,
                                                           @RequestParam(value = "fine") Long fineID)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.MEMBER, AccountStatus.ACTIVE);
        Fine fine = validationService.fineValidation(fineID);
        return memberService.payFine(member, fine, TransactionType.CREDIT_CARD,
                new CreditCardTransaction(request.getName()), request.getAmount());
    }

    @PutMapping("/fines/transaction/check")
    public ResponseEntity<MessageResponse> checkTransaction(@Valid @RequestBody CheckTransactionRequest request,
                                                            @RequestParam(value = "fine") Long fineID)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.MEMBER, AccountStatus.ACTIVE);
        Fine fine = validationService.fineValidation(fineID);
        return memberService.payFine(member, fine, TransactionType.CHECK,
                new CheckTransaction(request.getBankName(), request.getCheckNumber()), request.getAmount());
    }

    @PutMapping("/fines/transaction/cash")
    public ResponseEntity<MessageResponse> cashTransaction(@Valid @RequestBody CashTransactionRequest request,
                                                           @RequestParam(value = "fine") Long fineID)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.MEMBER, AccountStatus.ACTIVE);
        Fine fine = validationService.fineValidation(fineID);
        return memberService.payFine(member, fine, TransactionType.CASH,
                new CashTransaction(request.getCashTendered()), request.getCashTendered());
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<MessageResponse> cancelMembership(@Valid @RequestBody CancelMembershipRequest request)
    {
        LibraryCard libraryCard = validationService.cardValidation(request.getBarcode());
        return accountService.cancelMemberAccount(libraryCard,
                request.getNumber(), request.getPassword());
    }
}
