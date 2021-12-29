package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.AccountServiceImp;
import com.example.LibraryManagement.components.services.CatalogServiceImp;
import com.example.LibraryManagement.components.services.LibrarianServiceImp;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.io.requests.account_requests.librarian_requests.AddLibrarianRequest;
import com.example.LibraryManagement.models.io.requests.account_requests.BarcodeValidationRequest;
import com.example.LibraryManagement.models.io.requests.account_requests.librarian_requests.UpdateMemberStatusRequest;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
 * TODO: Add functions for adding and modifying book items, blocking members, and checking other member accounts.
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
    private final CatalogServiceImp catalogService;
    @Autowired
    private final LibrarianServiceImp librarianService;

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

    @GetMapping("/records/book_loans")
    public ResponseEntity<List<BookLending>> viewAllBookLendings(@Valid @RequestBody BarcodeValidationRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllBookLendings();
    }

    @GetMapping("/records/book_reservations")
    public ResponseEntity<List<BookReservation>> viewAllBookReservations(@Valid @RequestBody BarcodeValidationRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllBookReservations();
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

    @PutMapping("/account/member/{id}")
    public ResponseEntity<MessageResponse> updateMemberStatus(@PathVariable("id") String memberID,
                                                              @Valid @RequestBody UpdateMemberStatusRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return accountService.updateMemberStatus(memberID, request.getStatus());
    }

//    @PostMapping("/catalog")
//    public ResponseEntity<MessageResponse> addBookItem()
//    {
//
//    }


}
