package com.example.LibraryManagement.models.interfaces.services.catalogs;

import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Set;

// Methods used in a service component relating to updating the system's catalog.
public interface UpdateCatalogService
{
    ResponseEntity<MessageResponse> addLibrary(String name, String streetAddress, String city,
                                               String zipcode, String country);

    ResponseEntity<MessageResponse> addBookItem(Library library, Rack rack, String ISBN, String title,
                                                String publisher, String language, int numberOfPages,
                                                Author author, Set<Subject> subjects, BookFormat format,
                                                Date publicationDate, boolean isReferenceOnly, double price);

    ResponseEntity<MessageResponse> addSubject(String subject);

    ResponseEntity<MessageResponse> addAuthor(String author, String description);

    ResponseEntity<MessageResponse> modifyBookItem(BookItem book, String ISBN, String title, String publisher,
                                                   String language, int numberOfPages, Author author,
                                                   Set<Subject> subjects, BookFormat format, Date publicationDate,
                                                   boolean isReferenceOnly, double price);

    ResponseEntity<MessageResponse> moveBookItem(BookItem book, Library newLibrary, Rack r);

    ResponseEntity<MessageResponse> modifyAuthor(Author author, String description);

    ResponseEntity<MessageResponse> removeLibrary(Library library);

    ResponseEntity<MessageResponse> removeBookItem(BookItem book);

    ResponseEntity<MessageResponse> removeSubject(Subject subject);

    ResponseEntity<MessageResponse> removeAuthor(Author author);
}
