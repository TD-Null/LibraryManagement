package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.catalogs.ViewCatalogServiceImp;
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
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping("library_website/catalog")
public class CatalogController
{
    @Autowired
    private final ViewCatalogServiceImp viewCatalogService;

    @GetMapping("/library")
    public ResponseEntity<List<Library>> viewAllLibraries() { return viewCatalogService.listAllLibraries(); }

    @GetMapping
    public ResponseEntity<List<BookItem>> viewAllBooks() { return viewCatalogService.listAllBooks(); }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> viewAllSubjects() { return viewCatalogService.listAllSubjects(); }

    @GetMapping("/author")
    public ResponseEntity<List<Author>> viewAllAuthors() { return viewCatalogService.listAllAuthors(); }

    @GetMapping("/search")
    public ResponseEntity<List<BookItem>> searchBooks(@RequestParam(value = "library", required = false, defaultValue = "none") String library,
                                                      @RequestParam(value = "title", required = false, defaultValue = "none") String title,
                                                      @RequestParam(value = "author", required = false, defaultValue = "none") String author,
                                                      @RequestParam(value = "subjects", required = false, defaultValue = "none") List<String> subjects,
                                                      @RequestParam(value = "date", required = false) Date publicationDate)
    {
        return viewCatalogService.searchBooks(library, title, author, subjects, publicationDate);
    }

    @GetMapping("/library/{name}/books")
    public ResponseEntity<List<BookItem>> viewLibraryBooks(@PathVariable("name") String libraryName) { return viewCatalogService.listLibraryBooks(libraryName); }

    @GetMapping("/filter/title")
    public ResponseEntity<List<BookItem>> viewBooksByTitle(@RequestParam String title) { return viewCatalogService.searchBooksByTitle(title); }

    @GetMapping("filter/author")
    public ResponseEntity<List<BookItem>> viewBooksByAuthor(@RequestParam String name) { return viewCatalogService.searchBooksByAuthor(name); }

    @GetMapping("filter/subjects")
    public ResponseEntity<List<BookItem>> viewBooksBySubject(@RequestParam String subject) { return viewCatalogService.searchBooksBySubject(subject); }

    @GetMapping("filter/publication_date")
    public ResponseEntity<List<BookItem>> viewBooksByPubDate(@RequestParam Date publicationDate) { return viewCatalogService.searchBooksByPubDate(publicationDate); }
}
