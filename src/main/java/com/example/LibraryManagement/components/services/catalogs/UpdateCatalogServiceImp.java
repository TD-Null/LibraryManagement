package com.example.LibraryManagement.components.services.catalogs;

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
    private final ValidationService validationService;

    public ResponseEntity<MessageResponse> addLibrary(String name, String streetAddress, String city,
                                                      String zipcode, String country)
    {
        if(libraryRepository.existsById(name))
            throw new ApiRequestException("Library already exists within the system.",
                    HttpStatus.CONFLICT);

        libraryRepository.save(new Library(name, new Address(streetAddress, city, zipcode, country)));
        return ResponseEntity.ok(new MessageResponse("Library has been successfully added to the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> addBookItem(String libraryName, Rack rack, String ISBN, String title,
                                                       String publisher, String language, int numberOfPages,
                                                       String authorName, Set<String> subjects, BookFormat format,
                                                       Date publicationDate, boolean isReferenceOnly, double price)
    {
        Library library = validationService.libraryValidation(libraryName);
        Author author = validationService.authorValidation(authorName);

        BookItem bookItem = new BookItem(ISBN, title, publisher, language, numberOfPages,
                rack.getNumber(), rack.getLocation(), format, BookStatus.AVAILABLE, publicationDate,
                isReferenceOnly, price);
        bookItemRepository.save(bookItem);

        library.addBookItem(bookItem);
        bookItem.setLibrary(library);

        author.addBookItem(bookItem);
        bookItem.setAuthor(author);

        for(String s: subjects)
        {
            Subject subject = validationService.subjectValidation(s);
            subject.addBookItem(bookItem);
            bookItem.addSubject(subject);
        }

        return ResponseEntity.ok(new MessageResponse("Book has been successfully added to the system."));
    }

    public ResponseEntity<MessageResponse> addSubject(String subject)
    {
        if(subjectRepository.existsById(subject))
            throw new ApiRequestException("Subject already exists within the system.",
                    HttpStatus.CONFLICT);

        subjectRepository.save(new Subject(subject));
        return ResponseEntity.ok(new MessageResponse("Subject has been successfully added to the system."));
    }

    public ResponseEntity<MessageResponse> addAuthor(String author, String description)
    {
        if(authorRepository.existsById(author))
            throw new ApiRequestException("Author already exists within the system.",
                    HttpStatus.CONFLICT);

        authorRepository.save(new Author(author, description));
        return ResponseEntity.ok(new MessageResponse("Author has been successfully added to the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> modifyBookItem(Long barcode, String ISBN, String title,
                                                          String publisher, String language, int numberOfPages,
                                                          String authorName, Set<String> subjects, BookFormat format,
                                                          Date publicationDate, boolean isReferenceOnly, double price)
    {
        BookItem bookItem = validationService.bookValidation(barcode);

        bookItem.setISBN(ISBN);
        bookItem.setTitle(title);
        bookItem.setPublisher(publisher);
        bookItem.setLanguage(language);
        bookItem.setNumberOfPages(numberOfPages);
        bookItem.setFormat(format);
        bookItem.setPublicationDate(publicationDate);
        bookItem.setReferenceOnly(isReferenceOnly);
        bookItem.setPrice(price);

        Author author = validationService.authorValidation(authorName);
        Author prevAuthor = bookItem.getAuthor();

        if(!prevAuthor.equals(author))
        {
            prevAuthor.removeBookItem(bookItem);
            author.addBookItem(bookItem);
            bookItem.setAuthor(author);
        }

        Set<Subject> prevSubjects = bookItem.getSubjects();
        Set<Subject> newSubjects = new HashSet<>();

        for(String s: subjects)
        {
            newSubjects.add(validationService.subjectValidation(s));
        }

        for(Subject s: prevSubjects)
        {
            if(!newSubjects.contains(s))
            {
                s.removeBookItem(bookItem);
                bookItem.removeSubject(s);
            }
        }

        for(Subject s: newSubjects)
        {
            if(!prevSubjects.contains(s))
            {
                s.addBookItem(bookItem);
                bookItem.addSubject(s);
            }
        }

        return ResponseEntity.ok(new MessageResponse("Book has been successfully updated within the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> moveBookItem(Long barcode, String libraryName, Rack r)
    {
        BookItem book = validationService.bookValidation(barcode);
        Library prevLibrary = book.getLibrary();
        Library newLibrary = validationService.libraryValidation(libraryName);

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
    public ResponseEntity<MessageResponse> modifyAuthor(String name, String description)
    {
        if(!authorRepository.existsById(name))
            throw new ApiRequestException("Author does not exist within the system.", HttpStatus.BAD_REQUEST);

        Author author = authorRepository.getById(name);
        author.setDescription(description);
        return ResponseEntity.ok(new MessageResponse("Author has been updated successfully within the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> removeLibrary(String libraryName)
    {
        Library library = validationService.libraryValidation(libraryName);
        library.clearLibrary();

        libraryRepository.delete(library);
        return ResponseEntity.ok(new MessageResponse("Library has been successfully removed from the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> removeBookItem(Long barcode)
    {
        BookItem book = validationService.bookValidation(barcode);

        if(book.getCurrLoanMember() != null && book.getStatus() == BookStatus.LOANED)
            throw new ApiRequestException("This book is currently not available and is loaned to a member.", HttpStatus.ACCEPTED);

        else if(book.getCurrReservedMember() != null && book.getStatus() == BookStatus.RESERVED)
        {
            Member member = book.getCurrReservedMember();
            member.cancelReservedBookItem(book);
            member.sendNotification(new AccountNotification(new Date(), member.getEmail(), member.getAddress(),
                    "Sorry, but this book is being removed and cannot be reserved for this user. " +
                            "We apologize for this inconvenience."));
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
    public ResponseEntity<MessageResponse> removeSubject(String name)
    {
        if(!subjectRepository.existsById(name))
            throw new ApiRequestException("Subject does not exist within the system.",
                    HttpStatus.NOT_FOUND);

        Subject subject = subjectRepository.getById(name);
        subject.clearBooks();
        subjectRepository.delete(subject);
        return ResponseEntity.ok(new MessageResponse("Subject has been successfully removed from the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> removeAuthor(String name)
    {
        if(!authorRepository.existsById(name))
            throw new ApiRequestException("Author does not exist within the system.",
                    HttpStatus.NOT_FOUND);

        Author author = authorRepository.getById(name);

        if(!author.getBooks().isEmpty())
            throw new ApiRequestException("Author is still associated with books within the system.",
                    HttpStatus.CONFLICT);

        authorRepository.delete(author);
        return ResponseEntity.ok(new MessageResponse("Author has been successfully removed from the system."));
    }
}
