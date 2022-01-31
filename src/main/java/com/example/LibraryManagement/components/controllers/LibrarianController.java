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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
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
@Slf4j
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
    public ResponseEntity<List<Member>> viewAllMembers(HttpServletRequest httpServletRequest,
                                                       @RequestParam(value = "barcode") Long barcode,
                                                       @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_members = 0;
        ResponseEntity<List<Member>> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            accountService.barcodeReader(
                    card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            response = librarianService.listAllMembers();
            num_members = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Librarian has viewed all " + num_members + " members within the system.";

                else
                    message = "Librarian has no members available to view.";
            }

            else
                message = "Librarian was unable to obtain all member accounts.";

            librarianViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/account/librarian")
    public ResponseEntity<List<Librarian>> viewAllLibrarians(HttpServletRequest httpServletRequest,
                                                             @RequestParam(value = "barcode") Long barcode,
                                                             @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_librarians = 0;
        ResponseEntity<List<Librarian>> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            accountService.barcodeReader(
                    card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            response = librarianService.listAllLibrarians();
            num_librarians = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Librarian has viewed all " + num_librarians + " librarians within the system.";

                else
                    message = "Librarian has no librarians available to view.";
            }

            else
                message = "Librarian was unable to obtain all librarian accounts.";

            librarianViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @PostMapping("/account/librarian/register")
    public ResponseEntity<LibraryCard> registerLibrarian(HttpServletRequest httpServletRequest,
                                                         @Valid @RequestBody RegisterLibrarianRequest request)
    {
        boolean requestSuccess = false;
        ResponseEntity<LibraryCard> response;
        Instant start = Instant.now();

        try
        {
            response = accountService.registerLibrarian(
                    request.getName(), request.getPassword(),
                    request.getEmail(), request.getStreetAddress(),
                    request.getCity(), request.getZipcode(),
                    request.getCountry(), request.getPhoneNumber(),
                    new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(requestSuccess)
                message = "Librarian has been registered to the system.";

            else
                message = "Librarian failed to register to the system.";

            registerLibrarianRequestLog(httpServletRequest.getRequestURL().toString(), message,
                    request.getName(), request.getPassword(), request.getEmail(),
                    requestSuccess, time);
        }
    }

    @PostMapping("/account/librarian/add")
    public ResponseEntity<LibraryCard> addLibrarian(HttpServletRequest httpServletRequest,
                                                    @Valid @RequestBody AddLibrarianRequest request)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        ResponseEntity<LibraryCard> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            accountService.barcodeReader(
                    card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            response = accountService.registerLibrarian(
                    request.getName(), request.getPassword(),
                    request.getEmail(), request.getStreetAddress(),
                    request.getCity(), request.getZipcode(),
                    request.getCountry(), request.getPhoneNumber(),
                    new Date());
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(requestSuccess)
                message = "Librarian has registered another librarian to the system.";

            else
                message = "Librarian failed to register another librarian to the system.";

            message += " (Librarian:" +
                    " Card Barcode = " + request.getBarcode() +
                    ", Card Number = " + request.getNumber();

            if(cardValidationSuccess)
                message += " [Valid])";

            else
                message += " [Invalid])";

            registerLibrarianRequestLog(httpServletRequest.getRequestURL().toString(), message,
                    request.getName(), request.getPassword(), request.getEmail(),
                    requestSuccess, time);
        }
    }

    @DeleteMapping("account/librarian/remove")
    public ResponseEntity<MessageResponse> removeLibrarian(HttpServletRequest httpServletRequest,
                                                           @Valid @RequestBody RemoveLibrarianRequest request)
    {
        boolean cardValidationSuccess = false;
        boolean removedLibrarianValidationSuccess = false;
        boolean requestSuccess = false;
        ResponseEntity<MessageResponse> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    request.getBarcode(), request.getNumber());
            accountService.barcodeReader(
                    card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            LibraryCard removedLibrarianCard = validationService.cardValidation(
                    request.getLibrarianCardBarcode(),
                    request.getLibrarianCardNumber());
            removedLibrarianValidationSuccess = true;

            response = accountService.cancelLibrarianAccount(removedLibrarianCard);
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(requestSuccess)
                message = "Librarian has removed a librarian from the system.";

            else
                message = "Librarian failed to remove a librarian from the system.";

            message += " (Librarian:" +
                    " Card Barcode = " + request.getBarcode() +
                    ", Card Number = " + request.getNumber();

            if(cardValidationSuccess)
                message += " [Valid])";

            else
                message += " [Invalid])";

            removeLibrarianRequestLog(httpServletRequest.getRequestURL().toString(), message,
                    request.getLibrarianCardBarcode(), request.getLibrarianCardNumber(),
                    removedLibrarianValidationSuccess, requestSuccess, time);
        }
    }

    @PutMapping("/account/member/block")
    public ResponseEntity<MessageResponse> blockMember(HttpServletRequest httpServletRequest,
                                                       @Valid @RequestBody CardValidationRequest request,
                                                       @RequestParam(name = "member") Long memberId)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Member member = validationService.memberValidation(memberId);
        return accountService.updateMemberStatus(member, AccountStatus.BLACKLISTED);
    }

    @PutMapping("/account/member/unblock")
    public ResponseEntity<MessageResponse> unblockMember(HttpServletRequest httpServletRequest,
                                                         @Valid @RequestBody CardValidationRequest request,
                                                         @RequestParam(value = "member") Long memberId)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Member member = validationService.memberValidation(memberId);
        return accountService.updateMemberStatus(member, AccountStatus.ACTIVE);
    }

    @GetMapping("/records/book_loans")
    public ResponseEntity<List<BookLending>> viewAllBookLoans(HttpServletRequest httpServletRequest,
                                                              @RequestParam(value = "barcode") Long barcode,
                                                              @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_bookLoans = 0;
        ResponseEntity<List<BookLending>> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            accountService.barcodeReader(
                    card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            response = librarianService.listAllBookLoans();
            num_bookLoans = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Librarian has viewed all " + num_bookLoans + " book loans within the system.";

                else
                    message = "Librarian has no book loans available to view.";
            }

            else
                message = "Librarian was unable to obtain all book loans.";

            librarianViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/records/book_reservations")
    public ResponseEntity<List<BookReservation>> viewAllBookReservations(HttpServletRequest httpServletRequest,
                                                                         @RequestParam(value = "barcode") Long barcode,
                                                                         @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_bookReservations = 0;
        ResponseEntity<List<BookReservation>> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            accountService.barcodeReader(
                    card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            response = librarianService.listAllBookReservations();
            num_bookReservations = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Librarian has viewed all " + num_bookReservations + " book reservations within the system.";

                else
                    message = "Librarian has no book reservations available to view.";
            }

            else
                message = "Librarian was unable to obtain all book reservations.";

            librarianViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @GetMapping("/record/fines")
    public ResponseEntity<List<Fine>> viewAllFines(HttpServletRequest httpServletRequest,
                                                   @RequestParam(value = "barcode") Long barcode,
                                                   @RequestParam(value = "card") String number)
    {
        boolean cardValidationSuccess = false;
        boolean requestSuccess = false;
        int num_fines = 0;
        ResponseEntity<List<Fine>> response;
        Instant start = Instant.now();

        try
        {
            LibraryCard card = validationService.cardValidation(
                    barcode, number);
            accountService.barcodeReader(
                    card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);
            cardValidationSuccess = true;

            response = librarianService.listAllFines();
            num_fines = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(cardValidationSuccess)
            {
                if (requestSuccess)
                    message = "Librarian has viewed all " + num_fines + " fines within the system.";

                else
                    message = "Librarian has no fines available to view.";
            }

            else
                message = "Librarian was unable to obtain all book fines.";

            librarianViewRequestLog(httpServletRequest.getRequestURL().toString(),
                    message, barcode, number, cardValidationSuccess, requestSuccess,
                    time);
        }
    }

    @PostMapping("/catalog/library/add")
    public ResponseEntity<MessageResponse> addLibrary(HttpServletRequest httpServletRequest,
                                                      @Valid @RequestBody AddLibraryRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        validationService.addLibraryValidation(request.getLibraryName());
        return updateCatalogService.addLibrary(request.getLibraryName(), request.getStreetAddress(), request.getCity(),
                request.getZipcode(), request.getCountry());
    }

    @PostMapping("/catalog/book/add")
    public ResponseEntity<MessageResponse> addBookItem(HttpServletRequest httpServletRequest,
                                                       @Valid @RequestBody AddBookItemRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(card,
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
    public ResponseEntity<MessageResponse> addSubject(HttpServletRequest httpServletRequest,
                                                      @Valid @RequestBody SubjectRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        validationService.addSubjectValidation(request.getSubject());
        return updateCatalogService.addSubject(request.getSubject());
    }

    @PostMapping("catalog/author/add")
    public ResponseEntity<MessageResponse> addAuthor(HttpServletRequest httpServletRequest,
                                                     @Valid @RequestBody AddAuthorRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        validationService.addAuthorValidation(request.getAuthor());
        return updateCatalogService.addAuthor(request.getAuthor(), request.getDescription());
    }

    @PutMapping("/catalog/book/update")
    public ResponseEntity<MessageResponse> updateBookItem(HttpServletRequest httpServletRequest,
                                                          @Valid @RequestBody UpdateBookItemRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

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
    public ResponseEntity<MessageResponse> moveBookItem(HttpServletRequest httpServletRequest,
                                                        @Valid @RequestBody MoveBookItemRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        BookItem book = validationService.bookValidation(request.getBookBarcode());
        Library library = validationService.libraryValidation(request.getLibraryName());
        return updateCatalogService.moveBookItem(book, library,
                new Rack(request.getRack(), request.getLocation()));
    }

    @PutMapping("/catalog/author/update")
    public ResponseEntity<MessageResponse> updateAuthor(HttpServletRequest httpServletRequest,
                                                        @Valid @RequestBody UpdateAuthorRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Author author = validationService.authorValidation(request.getAuthor());
        return updateCatalogService.modifyAuthor(author, request.getDescription());
    }

    @DeleteMapping("catalog/library/remove")
    public ResponseEntity<MessageResponse> removeLibrary(HttpServletRequest httpServletRequest,
                                                         @Valid @RequestBody RemoveLibraryRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Library library = validationService.libraryValidation(request.getLibrary());
        return updateCatalogService.removeLibrary(library);
    }

    @DeleteMapping("/catalog/book/remove")
    public ResponseEntity<MessageResponse> removeBookItem(HttpServletRequest httpServletRequest,
                                                          @Valid @RequestBody RemoveBookItemRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        BookItem book = validationService.bookValidation(request.getBookBarcode());
        return updateCatalogService.removeBookItem(book);
    }

    @DeleteMapping("/catalog/subject/remove")
    public ResponseEntity<MessageResponse> removeSubject(HttpServletRequest httpServletRequest,
                                                         @Valid @RequestBody SubjectRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Subject subject = validationService.subjectValidation(request.getSubject());
        return updateCatalogService.removeSubject(subject);
    }

    @DeleteMapping("/catalog/author/remove")
    public ResponseEntity<MessageResponse> removeAuthor(HttpServletRequest httpServletRequest,
                                                        @Valid @RequestBody RemoveAuthorRequest request)
    {
        LibraryCard card = validationService.cardValidation(
                request.getBarcode(), request.getNumber());
        accountService.barcodeReader(
                card, AccountType.LIBRARIAN, AccountStatus.ACTIVE);

        Author author = validationService.authorValidation(request.getAuthor());
        return updateCatalogService.removeAuthor(author);
    }

    private void librarianViewRequestLog(String requestURL, String message, long barcode, String number,
                                         boolean cardValidation, boolean requestSuccess, long time)
    {
        String requestType = "GET";
        String userLog = "(Librarian:" +
                " Card Barcode = " + barcode +
                ", Card Number = " + number;
        String successLog = "(Success! Completed in " + time + " ms)";

        if(cardValidation)
            userLog += " [Valid])";

        else
            userLog += " [Invalid])";

        if(requestSuccess)
            successLog = "(Success! Completed in " + time + " ms)";

        else
            successLog = "(Failure! Completed in " + time + " ms)";

        log.info(requestType + " " + requestURL + " " + message + " " +
                userLog + " " + successLog);
    }

    private void registerLibrarianRequestLog(String requestURL, String message, String name, String password,
                                             String email, boolean requestSuccess, long time)
    {
        String requestType = "POST";
        String userLog = "(Added Librarian:" +
                " Name = " + name +
                ", Password = " + password +
                ", Email = " + email;
        String successLog;

        if(requestSuccess)
        {
            userLog += " [Valid])";
            successLog = "(Success! Completed in " + time + " ms)";
        }

        else
        {
            userLog += " [Invalid])";
            successLog = "(Failed! Completed in " + time + " ms)";
        }

        log.info(requestType + " " + requestURL + " " + message + " " +
                userLog + " " + successLog);
    }

    private void removeLibrarianRequestLog(String requestURL, String message, long barcode, String number,
                                           boolean cardValidation, boolean requestSuccess, long time)
    {
        String requestType = "DELETE";
        String userLog = "(Removed Librarian:" +
                " Card Barcode = " + barcode +
                ", Card Number = " + number;
        String successLog;

        if(cardValidation)
            userLog += " [Valid])";

        else
            userLog += " [Invalid])";

        if(requestSuccess)
        {
            successLog = "(Success! Completed in " + time + " ms)";
        }

        else
        {
            successLog = "(Failed! Completed in " + time + " ms)";
        }

        log.info(requestType + " " + requestURL + " " + message + " " +
                userLog + " " + successLog);
    }
}
