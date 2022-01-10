package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.books.*;
import com.example.LibraryManagement.components.repositories.books.libraries.LibraryRepository;
import com.example.LibraryManagement.components.repositories.books.libraries.RackRepository;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
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
import java.util.Optional;
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
    private final RackRepository rackRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final SubjectRepository subjectRepository;

    public ResponseEntity<MessageResponse> addLibrary(String name, String streetAddress, String city,
                                                      String zipcode, String country)
    {
        if(libraryRepository.existsById(name))
            throw new ApiRequestException("Library already exists within the system.",
                    HttpStatus.BAD_REQUEST);

        libraryRepository.save(new Library(name, new Address(streetAddress, city, zipcode, country)));
        return ResponseEntity.ok(new MessageResponse("Library has been successfully added to the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> addLibraryRack(String name, int number, String locationIdentifier)
    {
        Library library = libraryValidation(name);
        Rack rack = new Rack(number, locationIdentifier);
        rackRepository.save(rack);
        library.addRack(rack);

        return ResponseEntity.ok(new MessageResponse("Rack has been successfully added to the Library within the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> addBookItem(String libraryName, long rackID, String ISBN,
                                                       String title, String publisher, String language,
                                                       int numberOfPages, String authorName, Set<String> subjects,
                                                       BookFormat format, Date publicationDate, boolean isReferenceOnly,
                                                       double price)
    {
        Library library = libraryValidation(libraryName);
        Rack rack = rackValidation(library, rackID);
        Author author = authorValidation(authorName);

        BookItem bookItem = new BookItem(ISBN, title, publisher, language, numberOfPages,
                format, BookStatus.AVAILABLE, publicationDate, isReferenceOnly, price);
        bookItemRepository.save(bookItem);

        library.addBookItem(bookItem);
        rack.addBookItem(bookItem);
        author.addBookItem(bookItem);

        for(String sub: subjects)
        {
            Subject subject = subjectValidation(sub);
            subject.addBookItem(bookItem);
        }

        return ResponseEntity.ok(new MessageResponse("Book has been successfully added to the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> modifyBookItem(Long barcode, String ISBN, String title,
                                                          String publisher, String language, int numberOfPages,
                                                          String authorName, Set<String> subjects, BookFormat format,
                                                          Date publicationDate, boolean isReferenceOnly, double price)
    {
        BookItem bookItem = bookValidation(barcode);

        bookItem.setISBN(ISBN);
        bookItem.setTitle(title);
        bookItem.setPublisher(publisher);
        bookItem.setLanguage(language);
        bookItem.setNumberOfPages(numberOfPages);
        bookItem.setFormat(format);
        bookItem.setPublicationDate(publicationDate);
        bookItem.setReferenceOnly(isReferenceOnly);
        bookItem.setPrice(price);

        Author author = authorValidation(authorName);
        Author prevAuthor = bookItem.getAuthor();

        if(!prevAuthor.equals(author))
        {
            prevAuthor.removeBookItem(bookItem);
            author.addBookItem(bookItem);
        }

        Set<Subject> newSubjects = new HashSet<>();

        for(String s: subjects)
        {
            newSubjects.add(subjectValidation(s));
        }

        Set<Subject> prevSubjects = bookItem.getSubjects();

        for(Subject s: prevSubjects)
        {
            if(!newSubjects.contains(s))
            {
                s.removeBookItem(bookItem);
            }
        }

        for(Subject s: newSubjects)
        {
            if(!prevSubjects.contains(s))
            {
                s.addBookItem(bookItem);
            }
        }

        return ResponseEntity.ok(new MessageResponse("Book has been successfully updated within the system."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> removeBookItem(Long barcode)
    {
        BookItem book = bookValidation(barcode);

        if(book.getCurrLoanMember() != null && book.getStatus() == BookStatus.LOANED)
            throw new ApiRequestException("This book is currently not available and is loaned to a member.", HttpStatus.ACCEPTED);

        bookItemRepository.delete(book);
        return ResponseEntity.ok(new MessageResponse("Book has been successfully removed from the system."));
    }

    private Library libraryValidation(String name)
    {
        Optional<Library> library = libraryRepository.findById(name);

        if(library.isEmpty())
            throw new ApiRequestException("Unable to find this library.",
                    HttpStatus.BAD_REQUEST);

        return library.get();
    }

    private Rack rackValidation(Library library, long rackID)
    {
        if(!rackRepository.existsById(rackID))
            throw new ApiRequestException("This rack does not exist within the system",
                    HttpStatus.BAD_REQUEST);

        Rack rack = rackRepository.getById(rackID);

        Set<Rack> libraryRacks = library.getRacks();

        if(libraryRacks.isEmpty())
            throw new ApiRequestException("There are no racks available in this library",
                    HttpStatus.BAD_REQUEST);

        else if(!libraryRacks.contains(rack))
            throw new ApiRequestException("This rack is not present within this library.",
                    HttpStatus.BAD_REQUEST);

        return rack;
    }

    public BookItem bookValidation(Long barcode)
    {
        Optional<BookItem> bookItem = bookItemRepository.findById(barcode);

        if(bookItem.isEmpty())
            throw new ApiRequestException("Unable to find book within the system.",
                    HttpStatus.BAD_REQUEST);

        return bookItem.get();
    }

    private Author authorValidation(String name)
    {
        if(!authorRepository.existsById(name))
        {
            authorRepository.save(new Author(name));
        }

        return authorRepository.getById(name);
    }

    private Subject subjectValidation(String name)
    {
        if(!subjectRepository.existsById(name))
        {
            subjectRepository.save(new Subject(name));
        }

        return subjectRepository.getById(name);
    }
}
