package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.catalogs.ViewCatalogServiceImp;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/*
 * Controller component containing the API requests relating to the library catalog:
 *
 * - Viewing all books
 * - Viewing all libraries
 * - Viewing all subjects
 * - Viewing all authors
 * - Search books based on library, title, author name, subjects, publication date
 *   (include books based on parameters)
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping("library_website/catalog")
public class CatalogController
{
    @Autowired
    private final ViewCatalogServiceImp viewCatalogService;

    /*
     * All books viewing GET request.
     * Returns a 200 response code with all the books within the system.
     * Will return no books and a 404 response if there are no books found within the system.
     */
    @GetMapping
    public ResponseEntity<List<BookItem>> viewAllBooks() { return viewCatalogService.listAllBooks(); }

    /*
     * All libraries viewing GET request.
     * Returns a 200 response code with all the libraries within the system.
     * Will return no libraries and a 404 response if there are no libraries found within the system.
     */
    @GetMapping("/library")
    public ResponseEntity<List<Library>> viewAllLibraries() { return viewCatalogService.listAllLibraries(); }

    /*
     * All subjects viewing GET request.
     * Returns a 200 response code with all the subjects within the system.
     * Will return no subjects and a 404 response if there are no subjects found within the system.
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> viewAllSubjects() { return viewCatalogService.listAllSubjects(); }

    /*
     * All subjects viewing GET request.
     * Returns a 200 response code with all the subjects within the system.
     * Will return no authors and a 404 response if there are no authors found within the system.
     */
    @GetMapping("/author")
    public ResponseEntity<List<Author>> viewAllAuthors() { return viewCatalogService.listAllAuthors(); }

    /*
     * Book search GET request.
     * Utilizes multiple request parameters, none of which are required for this function.
     * These request parameters include:
     *
     * - Library name
     * - Book title
     * - Author name
     * - Subject names (can be multiple)
     * - Publication date
     *
     * Returns a 200 response code with the books found included under the given request parameters.
     * Will return no books and a 404 response if there are no books found within the given parameters.
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookItem>> searchBooks(@RequestParam(value = "library", required = false, defaultValue = "none") String library,
                                                      @RequestParam(value = "title", required = false, defaultValue = "none") String title,
                                                      @RequestParam(value = "author", required = false, defaultValue = "none") String author,
                                                      @RequestParam(value = "subjects", required = false, defaultValue = "none") List<String> subjects,
                                                      @RequestParam(value = "pub_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date publicationDate)
    {
        return viewCatalogService.searchBooks(library, title, author, subjects, publicationDate);
    }

}
