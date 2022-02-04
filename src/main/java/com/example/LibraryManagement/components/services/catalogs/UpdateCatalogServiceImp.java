package com.example.LibraryManagement.components.services.catalogs;

import com.example.LibraryManagement.components.repositories.accounts.AccountNotificationRepository;
import com.example.LibraryManagement.components.repositories.books.*;
import com.example.LibraryManagement.components.repositories.books.LibraryRepository;
import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import com.example.LibraryManagement.models.interfaces.services.catalogs.UpdateCatalogService;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UpdateCatalogServiceImp implements UpdateCatalogService
{
    @Autowired
    private final BookItemRepository bookItemRepository;
    @Autowired
    private final LibraryRepository libraryRepository;
    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final AccountNotificationRepository notificationRepository;

    public ResponseEntity<MessageResponse> addLibrary(String name, String streetAddress, String city,
                                                      String zipcode, String country)
    {
        libraryRepository.save(new Library(name, new Address(streetAddress, city, zipcode, country)));
        return new ResponseEntity<>(new MessageResponse("Library has been successfully added to the system."),
                HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<MessageResponse> addBookItem(Library library, Rack rack, String ISBN, String title,
                                                       String publisher, String language, int numberOfPages,
                                                       Author author, Set<Subject> subjects, BookFormat format,
                                                       Date publicationDate, boolean isReferenceOnly, double price)
    {
        BookItem bookItem = new BookItem(ISBN, title, publisher, language,
                numberOfPages, rack, format, BookStatus.AVAILABLE,
                publicationDate, isReferenceOnly, price);

        bookItemRepository.save(bookItem);

        bookItem.setLibrary(library);
        bookItem.setAuthor(author);

        library.addBookItem(bookItem);
        author.addBookItem(bookItem);

        for(Subject s: subjects)
        {
            s.addBookItem(bookItem);
            bookItem.addSubject(s);
        }

        return new ResponseEntity<>(new MessageResponse("Book has been successfully added to the system."),
                HttpStatus.CREATED);
    }

    public ResponseEntity<MessageResponse> addSubject(String subject)
    {
        subjectRepository.save(new Subject(subject));
        return new ResponseEntity<>(new MessageResponse("Subject has been successfully added to the system."),
                HttpStatus.CREATED);
    }

    public ResponseEntity<MessageResponse> addAuthor(String author, String description)
    {
        authorRepository.save(new Author(author, description));
        return new ResponseEntity<>(new MessageResponse("Author has been successfully added to the system."),
                HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<MessageResponse> modifyBookItem(BookItem book, String ISBN, String title,
                                                          String publisher, String language, int numberOfPages,
                                                          Author author, Set<Subject> subjects, BookFormat format,
                                                          Date publicationDate, boolean isReferenceOnly, double price)
    {
        // Set the new properties for the book item.
        book.setISBN(ISBN);
        book.setTitle(title);
        book.setPublisher(publisher);
        book.setLanguage(language);
        book.setNumberOfPages(numberOfPages);
        book.setFormat(format);
        book.setPublicationDate(publicationDate);
        book.setReferenceOnly(isReferenceOnly);
        book.setPrice(price);

        // If the new author is not the same as the current author of
        // the book item, set the new Author to the book.
        Author prevAuthor = book.getAuthor();

        if(!prevAuthor.equals(author))
        {
            prevAuthor.removeBookItem(book);
            author.addBookItem(book);
            book.setAuthor(author);
        }

        // Clear all subjects from the book and add in the
        // the new subjects.
        Set<Subject> prevSubjects = book.getSubjects();

        for(Subject s: prevSubjects)
        {
            s.removeBookItem(book);
        }

        book.clearSubjects();

        for(Subject s: subjects)
        {
            s.addBookItem(book);
            book.addSubject(s);
        }

        return ResponseEntity.ok(new MessageResponse("Book has been successfully updated within the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> moveBookItem(BookItem book, Library newLibrary, Rack r)
    {
        Library prevLibrary = book.getLibrary();

        if(!newLibrary.equals(prevLibrary))
        {
            prevLibrary.removeBookItem(book);
            newLibrary.addBookItem(book);
            book.setLibrary(newLibrary);
        }

        book.setRack(r);
        return ResponseEntity.ok(new MessageResponse("Book has been successfully moved within the system"));
    }

    @Transactional
    public ResponseEntity<MessageResponse> modifyAuthor(Author author, String description)
    {
        author.setDescription(description);
        return ResponseEntity.ok(new MessageResponse("Author has been updated successfully within the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> removeLibrary(Library library)
    {
        if(!library.getBooks().isEmpty())
            throw new ApiRequestException("Library still contains books within the system.",
                    HttpStatus.CONFLICT);

        libraryRepository.delete(library);
        return ResponseEntity.ok(new MessageResponse("Library has been successfully removed from the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> removeBookItem(BookItem book)
    {
        if(book.getCurrLoanMember() != null && book.getStatus() == BookStatus.LOANED)
            throw new ApiRequestException("This book is currently not available and is loaned to a member.",
                    HttpStatus.CONFLICT);

        else if(book.getCurrReservedMember() != null && book.getStatus() == BookStatus.RESERVED)
        {
            Member member = book.getCurrReservedMember();
            member.cancelReservedBookItem(book);

            AccountNotification notification = new AccountNotification(member,
                    new Date(), member.getEmail(), member.getAddress(),
                    "Sorry, but this book is being removed and cannot be reserved for this user. " +
                            "We apologize for this inconvenience.");

            notificationRepository.save(notification);
            member.sendNotification(notification);
        }

        Library library = book.getLibrary();

        if(library != null)
        {
            library.removeBookItem(book);
            book.setLibrary(null);
        }

        Author author = book.getAuthor();

        if(author != null)
        {
            author.removeBookItem(book);
            book.setAuthor(null);
        }

        Set<Subject> subjects = book.getSubjects();

        for(Subject s: subjects)
            s.removeBookItem(book);

        book.clearSubjects();
        book.clearRecords();

        bookItemRepository.delete(book);
        return ResponseEntity.ok(new MessageResponse("Book has been successfully removed from the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> removeSubject(Subject subject)
    {
        Set<BookItem> subjectBooks = subject.getBooks();

        for(BookItem b: subjectBooks)
            b.removeSubject(subject);

        subject.clearBooks();
        subjectRepository.delete(subject);
        return ResponseEntity.ok(new MessageResponse("Subject has been successfully removed from the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> removeAuthor(Author author)
    {
        if(!author.getBooks().isEmpty())
            throw new ApiRequestException("Author is still associated with books within the system.",
                    HttpStatus.CONFLICT);

        authorRepository.delete(author);
        return ResponseEntity.ok(new MessageResponse("Author has been successfully removed from the system."));
    }
}
