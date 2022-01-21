package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.components.services.catalogs.UpdateCatalogServiceImp;
import com.example.LibraryManagement.components.services.accounts.LibrarianServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.*;
import com.example.LibraryManagement.models.io.requests.librarian_requests.delete.RemoveAuthorRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.delete.RemoveBookItemRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.delete.RemoveLibrarianRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.delete.RemoveLibraryRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.*;
import com.example.LibraryManagement.models.io.requests.librarian_requests.put.MoveBookItemRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.put.UpdateAuthorRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.put.UpdateBookItemRequest;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Date;
import java.util.List;

/*
 * Controller component containing the API requests relating to Librarians:
 *
 * - Viewing all members/librarians
 * - Registering/adding and removing librarians
 * - Block/unblock members
 * - Viewing all member book loans/reservations and fines
 * - Add/remove and modify libraries, books, subjects, and authors
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
    @Autowired
    private final ValidationService validationService;

    @GetMapping("/account/member")
    public ResponseEntity<List<Member>> viewAllMembers(@RequestParam(value = "barcode") Long barcode,
                                                       @RequestParam(value = "card") String number)
    {
        accountService.barcodeReader(barcode, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllMembers();
    }

    @GetMapping("/account/librarian")
    public ResponseEntity<List<Librarian>> viewAllLibrarians(@RequestParam(value = "barcode") Long barcode,
                                                             @RequestParam(value = "card") String number)
    {
        accountService.barcodeReader(barcode, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllLibrarians();
    }

    @PostMapping("/account/librarian/register")
    public ResponseEntity<LibraryCard> registerLibrarian(@Valid @RequestBody RegisterLibrarianRequest request)
    {
        return accountService.registerLibrarian(
                request.getName(), request.getPassword(),
                request.getEmail(), request.getStreetAddress(),
                request.getCity(), request.getZipcode(),
                request.getCountry(), request.getPhoneNumber(),
                new Date());
    }

    @PostMapping("/account/librarian/add")
    public ResponseEntity<LibraryCard> addLibrarian(@Valid @RequestBody AddLibrarianRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return accountService.registerLibrarian(
                request.getName(), request.getPassword(),
                request.getEmail(), request.getStreetAddress(),
                request.getCity(), request.getZipcode(),
                request.getCountry(), request.getPhoneNumber(),
                new Date());
    }

    @DeleteMapping("account/librarian/remove")
    public ResponseEntity<MessageResponse> removeLibrarian(@Valid @RequestBody RemoveLibrarianRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        LibraryCard libraryCard = validationService.cardValidation(request.getLibrarianCardBarcode());
        return accountService.cancelLibrarianAccount(libraryCard, request.getLibrarianCardNumber());
    }

    @PutMapping("/account/member/block")
    public ResponseEntity<MessageResponse> blockMember(@Valid @RequestBody CardValidationRequest request,
                                                       @RequestParam(name = "member") Long memberId)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return accountService.updateMemberStatus(memberId, AccountStatus.BLACKLISTED);
    }

    @PutMapping("/account/member/unblock")
    public ResponseEntity<MessageResponse> unblockMember(@Valid @RequestBody CardValidationRequest request,
                                                         @RequestParam(value = "member") Long memberId)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return accountService.updateMemberStatus(memberId, AccountStatus.ACTIVE);
    }

    @GetMapping("/records/book_loans")
    public ResponseEntity<List<BookLending>> viewAllBookLoans(@RequestParam(value = "barcode") Long barcode,
                                                              @RequestParam(value = "card") String number)
    {
        accountService.barcodeReader(barcode, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllBookLoans();
    }

    @GetMapping("/records/book_reservations")
    public ResponseEntity<List<BookReservation>> viewAllBookReservations(@RequestParam(value = "barcode") Long barcode,
                                                                         @RequestParam(value = "card") String number)
    {
        accountService.barcodeReader(barcode, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllBookReservations();
    }

    @GetMapping("/record/fines")
    public ResponseEntity<List<Fine>> viewAllFines(@RequestParam(value = "barcode") Long barcode,
                                                   @RequestParam(value = "card") String number)
    {
        accountService.barcodeReader(barcode, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return librarianService.listAllFines();
    }

    @PostMapping("/catalog/library/add")
    public ResponseEntity<MessageResponse> addLibrary(@Valid @RequestBody AddLibraryRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.addLibrary(request.getLibraryName(), request.getStreetAddress(), request.getCity(),
                request.getZipcode(), request.getCountry());
    }

    @PostMapping("/catalog/book/add")
    public ResponseEntity<MessageResponse> addBookItem(@Valid @RequestBody AddBookItemRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.addBookItem(request.getLibraryName(), new Rack(request.getRack(), request.getLocation()),
                request.getIsbn(), request.getTitle(), request.getPublisher(), request.getLanguage(), request.getNumberOfPages(),
                request.getAuthor(), request.getSubjectNames(), request.getFormat(), request.getPublicationDate(),
                request.isReferenceOnly(), request.getPrice());
    }

    @PostMapping("catalog/subject/add")
    public ResponseEntity<MessageResponse> addSubject(@Valid @RequestBody SubjectRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.addSubject(request.getSubject());
    }

    @PostMapping("catalog/author/add")
    public ResponseEntity<MessageResponse> addAuthor(@Valid @RequestBody AddAuthorRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.addAuthor(request.getAuthor(), request.getDescription());
    }

    @PutMapping("/catalog/book/update")
    public ResponseEntity<MessageResponse> updateBookItem(@Valid @RequestBody UpdateBookItemRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.modifyBookItem(request.getBookBarcode(), request.getIsbn(), request.getTitle(),
                request.getPublisher(), request.getLanguage(), request.getNumberOfPages(), request.getAuthor(),
                request.getSubjectNames(), request.getFormat(), request.getPublicationDate(), request.isReferenceOnly(),
                request.getPrice());
    }

    @PutMapping("/catalog/book/move")
    public ResponseEntity<MessageResponse> moveBookItem(@Valid @RequestBody MoveBookItemRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.moveBookItem(request.getBookBarcode(), request.getLibraryName(),
                new Rack(request.getRack(), request.getLocation()));
    }

    @PutMapping("/catalog/author/update")
    public ResponseEntity<MessageResponse> updateAuthor(@Valid @RequestBody UpdateAuthorRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.modifyAuthor(request.getAuthor(), request.getDescription());
    }

    @DeleteMapping("catalog/library/remove")
    public ResponseEntity<MessageResponse> removeLibrary(@Valid @RequestBody RemoveLibraryRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.removeLibrary(request.getLibrary());
    }

    @DeleteMapping("/catalog/book/remove")
    public ResponseEntity<MessageResponse> removeBookItem(@Valid @RequestBody RemoveBookItemRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.removeBookItem(request.getBookBarcode());
    }

    @DeleteMapping("/catalog/subject/remove")
    public ResponseEntity<MessageResponse> removeSubject(@Valid @RequestBody SubjectRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.removeSubject(request.getSubject());
    }

    @DeleteMapping("/catalog/author/remove")
    public ResponseEntity<MessageResponse> removeAuthor(@Valid @RequestBody RemoveAuthorRequest request)
    {
        accountService.barcodeReader(request.getBarcode(), request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);
        return updateCatalogService.removeAuthor(request.getAuthor());
    }
}
