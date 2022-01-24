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
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        LibraryCard card = validationService.cardValidation(barcode);
        accountService.barcodeReader(card, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        return librarianService.listAllMembers();
    }

    @GetMapping("/account/librarian")
    public ResponseEntity<List<Librarian>> viewAllLibrarians(@RequestParam(value = "barcode") Long barcode,
                                                             @RequestParam(value = "card") String number)
    {
        LibraryCard card = validationService.cardValidation(barcode);
        accountService.barcodeReader(card, number,
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
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
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
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        LibraryCard libraryCard = validationService.cardValidation(request.getLibrarianCardBarcode());
        return accountService.cancelLibrarianAccount(libraryCard, request.getLibrarianCardNumber());
    }

    @PutMapping("/account/member/block")
    public ResponseEntity<MessageResponse> blockMember(@Valid @RequestBody CardValidationRequest request,
                                                       @RequestParam(name = "member") Long memberId)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Member member = validationService.memberValidation(memberId);
        return accountService.updateMemberStatus(member, AccountStatus.BLACKLISTED);
    }

    @PutMapping("/account/member/unblock")
    public ResponseEntity<MessageResponse> unblockMember(@Valid @RequestBody CardValidationRequest request,
                                                         @RequestParam(value = "member") Long memberId)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Member member = validationService.memberValidation(memberId);
        return accountService.updateMemberStatus(member, AccountStatus.ACTIVE);
    }

    @GetMapping("/records/book_loans")
    public ResponseEntity<List<BookLending>> viewAllBookLoans(@RequestParam(value = "barcode") Long barcode,
                                                              @RequestParam(value = "card") String number)
    {
        LibraryCard card = validationService.cardValidation(barcode);
        accountService.barcodeReader(card, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        return librarianService.listAllBookLoans();
    }

    @GetMapping("/records/book_reservations")
    public ResponseEntity<List<BookReservation>> viewAllBookReservations(@RequestParam(value = "barcode") Long barcode,
                                                                         @RequestParam(value = "card") String number)
    {
        LibraryCard card = validationService.cardValidation(barcode);
        accountService.barcodeReader(card, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        return librarianService.listAllBookReservations();
    }

    @GetMapping("/record/fines")
    public ResponseEntity<List<Fine>> viewAllFines(@RequestParam(value = "barcode") Long barcode,
                                                   @RequestParam(value = "card") String number)
    {
        LibraryCard card = validationService.cardValidation(barcode);
        accountService.barcodeReader(card, number,
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        return librarianService.listAllFines();
    }

    @PostMapping("/catalog/library/add")
    public ResponseEntity<MessageResponse> addLibrary(@Valid @RequestBody AddLibraryRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        return updateCatalogService.addLibrary(request.getLibraryName(), request.getStreetAddress(), request.getCity(),
                request.getZipcode(), request.getCountry());
    }

    @PostMapping("/catalog/book/add")
    public ResponseEntity<MessageResponse> addBookItem(@Valid @RequestBody AddBookItemRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Library library = validationService.libraryValidation(request.getLibraryName());
        Author author = validationService.addBookAuthorValidation(request.getAuthor());

        Set<String> subjectNames = request.getSubjectNames();
        Set<Subject> subjects = new HashSet<>();

        for(String name: subjectNames)
        {
            subjects.add(validationService.addBookSubjectValidation(name));
        }

        return updateCatalogService.addBookItem(library, new Rack(request.getRack(), request.getLocation()),
                request.getIsbn(), request.getTitle(), request.getPublisher(), request.getLanguage(),
                request.getNumberOfPages(), author, subjects, request.getFormat(), request.getPublicationDate(),
                request.isReferenceOnly(), request.getPrice());
    }

    @PostMapping("catalog/subject/add")
    public ResponseEntity<MessageResponse> addSubject(@Valid @RequestBody SubjectRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        validationService.addSubjectValidation(request.getSubject());
        return updateCatalogService.addSubject(request.getSubject());
    }

    @PostMapping("catalog/author/add")
    public ResponseEntity<MessageResponse> addAuthor(@Valid @RequestBody AddAuthorRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        validationService.addAuthorValidation(request.getAuthor());
        return updateCatalogService.addAuthor(request.getAuthor(), request.getDescription());
    }

    @PutMapping("/catalog/book/update")
    public ResponseEntity<MessageResponse> updateBookItem(@Valid @RequestBody UpdateBookItemRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        BookItem book = validationService.bookValidation(request.getBookBarcode());
        Author author = validationService.addBookAuthorValidation(request.getAuthor());

        Set<String> subjectNames = request.getSubjectNames();
        Set<Subject> subjects = new HashSet<>();

        for(String name: subjectNames)
        {
            subjects.add(validationService.addBookSubjectValidation(name));
        }

        return updateCatalogService.modifyBookItem(book, request.getIsbn(), request.getTitle(),
                request.getPublisher(), request.getLanguage(), request.getNumberOfPages(), author,
                subjects, request.getFormat(), request.getPublicationDate(), request.isReferenceOnly(),
                request.getPrice());
    }

    @PutMapping("/catalog/book/move")
    public ResponseEntity<MessageResponse> moveBookItem(@Valid @RequestBody MoveBookItemRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        BookItem book = validationService.bookValidation(request.getBookBarcode());
        Library library = validationService.libraryValidation(request.getLibraryName());
        return updateCatalogService.moveBookItem(book, library,
                new Rack(request.getRack(), request.getLocation()));
    }

    @PutMapping("/catalog/author/update")
    public ResponseEntity<MessageResponse> updateAuthor(@Valid @RequestBody UpdateAuthorRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Author author = validationService.authorValidation(request.getAuthor());
        return updateCatalogService.modifyAuthor(author, request.getDescription());
    }

    @DeleteMapping("catalog/library/remove")
    public ResponseEntity<MessageResponse> removeLibrary(@Valid @RequestBody RemoveLibraryRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Library library = validationService.libraryValidation(request.getLibrary());
        return updateCatalogService.removeLibrary(library);
    }

    @DeleteMapping("/catalog/book/remove")
    public ResponseEntity<MessageResponse> removeBookItem(@Valid @RequestBody RemoveBookItemRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        BookItem book = validationService.bookValidation(request.getBookBarcode());
        return updateCatalogService.removeBookItem(book);
    }

    @DeleteMapping("/catalog/subject/remove")
    public ResponseEntity<MessageResponse> removeSubject(@Valid @RequestBody SubjectRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Subject subject = validationService.subjectValidation(request.getSubject());
        return updateCatalogService.removeSubject(subject);
    }

    @DeleteMapping("/catalog/author/remove")
    public ResponseEntity<MessageResponse> removeAuthor(@Valid @RequestBody RemoveAuthorRequest request)
    {
        LibraryCard card = validationService.cardValidation(request.getBarcode());
        accountService.barcodeReader(card, request.getNumber(),
                AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Author author = validationService.authorValidation(request.getAuthor());
        return updateCatalogService.removeAuthor(author);
    }
}
