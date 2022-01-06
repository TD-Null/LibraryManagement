package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.AccountServiceImp;
import com.example.LibraryManagement.components.services.MemberServiceImp;
import com.example.LibraryManagement.components.services.UpdateCatalogServiceImp;
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
import com.example.LibraryManagement.models.io.requests.account_requests.BarcodeValidationRequest;
import com.example.LibraryManagement.models.io.requests.account_requests.member_requests.CardTransactionRequest;
import com.example.LibraryManagement.models.io.requests.account_requests.member_requests.CashTransactionRequest;
import com.example.LibraryManagement.models.io.requests.account_requests.member_requests.CheckTransactionRequest;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/*
 * TODO: Add functions for borrowing, reserving, renewing books, as well check their account's details.
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
    private final UpdateCatalogServiceImp updateCatalogService;
    @Autowired
    private final MemberServiceImp memberService;

    @GetMapping("/checkout/books")
    public ResponseEntity<List<BookItem>> viewBooksLoans(@Valid @RequestBody BarcodeValidationRequest request)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        List<BookItem> bookLoans = new ArrayList<>(member.getCheckedOutBooks());

        if(bookLoans.isEmpty())
            throw new ApiRequestException("Member has no books borrowed currently.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(bookLoans);
    }

    @GetMapping("/reserve/books")
    public ResponseEntity<List<BookItem>> viewReservedBooks(@Valid @RequestBody BarcodeValidationRequest request)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        List<BookItem> bookReservations = new ArrayList<>(member.getReservedBooks());

        if(bookReservations.isEmpty())
            throw new ApiRequestException("Member has no books reserved currently.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(bookReservations);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<AccountNotification>> viewNotifications(@Valid @RequestBody BarcodeValidationRequest request)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        List<AccountNotification> notifications = new ArrayList<>(member.getNotifications());

        if(notifications.isEmpty())
            throw new ApiRequestException("Member has no notifications currently.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/fines")
    public ResponseEntity<List<Fine>> viewFines(@Valid @RequestBody BarcodeValidationRequest request)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        List<Fine> fines = new ArrayList<>(member.getFines());

        if(fines.isEmpty())
            throw new ApiRequestException("Member has no fines currently.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(fines);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<FineTransaction>> viewTransactions(@Valid @RequestBody BarcodeValidationRequest request)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
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

        if(!transactions.isEmpty())
            throw new ApiRequestException("Member has not made any transactions currently.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/checkout/history")
    public ResponseEntity<List<BookLending>> viewCheckoutHistory(@Valid @RequestBody BarcodeValidationRequest request)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        List<BookLending> checkoutRecords = new ArrayList<>(member.getBookLoans());

        if(!checkoutRecords.isEmpty())
            throw new ApiRequestException("Member has no history of books checked out.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(checkoutRecords);
    }

    @GetMapping("/reserve/history")
    public ResponseEntity<List<BookReservation>> viewReservationHistory(@Valid @RequestBody BarcodeValidationRequest request)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        List<BookReservation> reservationRecords = new ArrayList<>(member.getBookReservations());

        if(!reservationRecords.isEmpty())
            throw new ApiRequestException("Member has no history of book reservations.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(reservationRecords);
    }

    @PutMapping("/checkout")
    public ResponseEntity<BookItem> checkoutBook(@Valid @RequestBody BarcodeValidationRequest request,
                                                 @RequestParam String bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = updateCatalogService.bookValidation(bookBarcode);
        return memberService.checkoutBook(member, book);
    }

    @PutMapping("/return")
    public ResponseEntity<MessageResponse> returnBook(@Valid @RequestBody BarcodeValidationRequest request,
                                                      @RequestParam String bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = updateCatalogService.bookValidation(bookBarcode);
        return memberService.returnBook(member, book);
    }

    @PutMapping("/reserve")
    public ResponseEntity<MessageResponse> reserveBook(@Valid @RequestBody BarcodeValidationRequest request,
                                                       @RequestParam String bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = updateCatalogService.bookValidation(bookBarcode);
        return memberService.reserveBook(member, book);
    }

    @PutMapping("/reserve/cancel")
    public ResponseEntity<MessageResponse> cancelReservation(@Valid @RequestBody BarcodeValidationRequest request,
                                                             @RequestParam String bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = updateCatalogService.bookValidation(bookBarcode);
        return memberService.cancelReservation(member, book);
    }

    @PutMapping("/renew")
    public ResponseEntity<MessageResponse> renewBook(@Valid @RequestBody BarcodeValidationRequest request,
                                                     @RequestParam String bookBarcode)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        BookItem book = updateCatalogService.bookValidation(bookBarcode);
        return memberService.renewBook(member, book);
    }

    @PutMapping("/fines/transaction/card")
    public ResponseEntity<MessageResponse> cardTransaction(@Valid @RequestBody CardTransactionRequest request,
                                                           @RequestParam Long fineID)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        return memberService.payFine(member, fineID, TransactionType.CREDIT_CARD,
                new CreditCardTransaction(request.getName()), request.getAmount());
    }

    @PutMapping("/fines/transaction/check")
    public ResponseEntity<MessageResponse> checkTransaction(@Valid @RequestBody CheckTransactionRequest request,
                                                            @RequestParam Long fineID)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        return memberService.payFine(member, fineID, TransactionType.CHECK,
                new CheckTransaction(request.getBankName(), request.getCheckNumber()), request.getAmount());
    }

    @PutMapping("/fines/transaction/cash")
    public ResponseEntity<MessageResponse> cashTransaction(@Valid @RequestBody CashTransactionRequest request,
                                                           @RequestParam Long fineID)
    {
        Member member = (Member) accountService.barcodeReader(request.getBarcode(), AccountType.MEMBER, AccountStatus.ACTIVE);
        return memberService.payFine(member, fineID, TransactionType.CASH,
                new CashTransaction(request.getCashTendered()), request.getCashTendered());
    }
}
