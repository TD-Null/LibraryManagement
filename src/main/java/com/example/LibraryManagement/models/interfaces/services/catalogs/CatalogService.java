package com.example.LibraryManagement.models.interfaces.services.catalogs;

import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

// Methods used in a service component relating to the catalog.
public interface CatalogService
{
    ResponseEntity<List<Library>> listAllLibraries();

    ResponseEntity<List<BookItem>> listLibraryBooks(String name);

    ResponseEntity<List<BookItem>> listLibraryRackBooks(String name, int number);

    ResponseEntity<List<BookItem>> listAllBooks();

    ResponseEntity<List<Subject>> listAllSubjects();

    ResponseEntity<List<Author>> listAllAuthors();

    ResponseEntity<List<BookItem>> searchBooksByTitle(String title);

    ResponseEntity<List<BookItem>> searchBooksByAuthor(String name);

    ResponseEntity<List<BookItem>> searchBooksBySubject(String subject);

    ResponseEntity<List<BookItem>> searchBooksByPubDate(Date publicationDate);
}
