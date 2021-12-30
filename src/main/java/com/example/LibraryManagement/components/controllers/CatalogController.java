package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.CatalogServiceImp;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

/*
 *
 * TODO: Rework the catalog to use a single function to filter the selection of book items given multiple inputs (title, author, subjects, pub date)
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping("library_website/catalog")
public class CatalogController
{
    @Autowired
    private final CatalogServiceImp catalogService;

    @GetMapping("/library")
    public ResponseEntity<List<Library>> viewAllLibraries() { return catalogService.listAllLibraries(); }

    @GetMapping("/library/{name}/books")
    public ResponseEntity<List<BookItem>> viewLibraryBooks(@PathVariable("name") String libraryName) { return catalogService.listLibraryBooks(libraryName); }

    @GetMapping("/library/{name}/rack/{number}")
    public ResponseEntity<List<BookItem>> viewLibraryRackBooks(@PathVariable("name") String libraryName,
                                                               @PathVariable("number") int rackNumber)
    {
        return catalogService.listLibraryRackBooks(libraryName, rackNumber);
    }

    @GetMapping
    public ResponseEntity<List<BookItem>> viewAllBooks()
    {
        return catalogService.listAllBooks();
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> viewAllSubjects() { return catalogService.listAllSubjects(); }

    @GetMapping("/author")
    public ResponseEntity<List<Author>> viewAllAuthors() { return catalogService.listAllAuthors(); }

    @GetMapping("/filter/title")
    public ResponseEntity<List<BookItem>> viewBooksByTitle(@RequestParam String title) { return catalogService.searchBooksByTitle(title); }

    @GetMapping("filter/author")
    public ResponseEntity<List<BookItem>> viewBooksByAuthor(@RequestParam String name) { return catalogService.searchBooksByAuthor(name); }

    @GetMapping("filter/subjects")
    public ResponseEntity<List<BookItem>> viewBooksBySubject(@RequestParam String subject) { return catalogService.searchBooksBySubject(subject); }

    @GetMapping("filter/publication_date")
    public ResponseEntity<List<BookItem>> viewBooksByPubDate(@RequestParam Date publicationDate) { return catalogService.searchBooksByPubDate(publicationDate); }
}
