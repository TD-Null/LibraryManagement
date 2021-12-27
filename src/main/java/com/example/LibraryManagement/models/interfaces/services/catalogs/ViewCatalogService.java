package com.example.LibraryManagement.models.interfaces.services.catalogs;

import com.example.LibraryManagement.models.books.properties.BookItem;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

public interface ViewCatalogService
{
    public ResponseEntity<List<BookItem>> listAllBooks();

    public ResponseEntity<List<BookItem>> searchBooksByTitle(String title);

    public ResponseEntity<List<BookItem>> searchBooksByAuthor(String name);

    public ResponseEntity<List<BookItem>> searchBooksBySubject(String subject);

    public ResponseEntity<List<BookItem>> searchBooksByPubDate(Date publicationDate);
}
