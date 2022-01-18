package com.example.LibraryManagement.models.interfaces.services.catalogs;

import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Set;

// Methods used in a service component relating to viewing the system's catalog.
public interface ViewCatalogService
{
    ResponseEntity<List<BookItem>> listAllBooks();

    ResponseEntity<List<Library>> listAllLibraries();

    ResponseEntity<List<Subject>> listAllSubjects();

    ResponseEntity<List<Author>> listAllAuthors();

    ResponseEntity<List<BookItem>> searchBooks(String libraryName,
                                               String title,
                                               String authorName,
                                               List<String> subjects,
                                               Date publicationDate);
}
