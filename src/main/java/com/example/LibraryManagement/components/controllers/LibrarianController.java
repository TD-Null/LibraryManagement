package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.AccountServiceImp;
import com.example.LibraryManagement.components.services.UpdateCatalogServiceImp;
import com.example.LibraryManagement.components.services.LibrarianServiceImp;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.io.requests.BarcodeValidationRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.*;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping("library_website")
public class LibrarianController
{
    @Autowired
    private final AccountServiceImp accountService;
    @Autowired
    private final LibrarianServiceImp librarianService;
    @Autowired
    private final UpdateCatalogServiceImp updateCatalogService;

    @GetMapping("/account/librarian")
    public ResponseEntity<List<Librarian>> viewAllLibrarians(@Valid @RequestBody BarcodeValidationRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllLibrarians();
    }

    @GetMapping("/account/member")
    public ResponseEntity<List<Member>> viewAllMembers(@Valid @RequestBody BarcodeValidationRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllMembers();
    }

    @PutMapping("/account/member/{id}")
    public ResponseEntity<MessageResponse> updateMemberStatus(@PathVariable("id") Long memberID,
                                                              @Valid @RequestBody UpdateMemberStatusRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return accountService.updateMemberStatus(memberID, request.getStatus());
    }

    @GetMapping("/records/book_loans")
    public ResponseEntity<List<BookLending>> viewAllBookLoans(@Valid @RequestBody BarcodeValidationRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllBookLoans();
    }

    @GetMapping("/records/book_reservations")
    public ResponseEntity<List<BookReservation>> viewAllBookReservations(@Valid @RequestBody BarcodeValidationRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllBookReservations();
    }

    @GetMapping("/record/fines")
    public ResponseEntity<List<Fine>> viewAllFines(@Valid @RequestBody BarcodeValidationRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllFines();
    }

    @PostMapping("/account/librarian")
    public ResponseEntity<MessageResponse> addLibrarian(@Valid @RequestBody AddLibrarianRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return accountService.registerLibrarian(
                request.getName(), request.getPassword(),
                request.getEmail(), request.getStreetAddress(),
                request.getCity(), request.getZipcode(),
                request.getCountry(), request.getPhoneNumber());
    }

    @PostMapping("/catalog/library")
    public ResponseEntity<MessageResponse> addLibrary(@Valid @RequestBody AddLibraryRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.addLibrary(request.getLibraryName(), request.getStreetAddress(), request.getCity(),
                request.getZipcode(), request.getCountry());
    }

    @PostMapping("/catalog/library/rack")
    public ResponseEntity<MessageResponse> addLibraryRack(@Valid @RequestBody AddLibraryRackRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.addLibraryRack(request.getLibraryName(), request.getNumber(), request.getLocationIdentifier());
    }

    @PostMapping("/catalog/add")
    public ResponseEntity<MessageResponse> addBookItem(@Valid @RequestBody AddBookItemRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.addBookItem(request.getLibraryName(), request.getRackID(), request.getISBN(),
                request.getTitle(), request.getPublisher(), request.getLanguage(), request.getNumberOfPages(),
                request.getAuthorName(), request.getSubjectNames(), request.getFormat(), request.getPublicationDate(),
                request.isReferenceOnly(), request.getPrice());
    }

    @PutMapping("/catalog/update")
    public ResponseEntity<MessageResponse> updateBookItem(@Valid @RequestBody UpdateBookItemRequest request,
                                                          @RequestParam Long barcode)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.modifyBookItem(barcode, request.getISBN(), request.getTitle(), request.getPublisher(),
                request.getLanguage(), request.getNumberOfPages(), request.getAuthorName(), request.getSubjectNames(),
                request.getFormat(), request.getPublicationDate(), request.isReferenceOnly(), request.getPrice());
    }
}
