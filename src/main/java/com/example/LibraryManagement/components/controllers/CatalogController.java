package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.catalogs.ViewCatalogServiceImp;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
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
@Slf4j
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
    public ResponseEntity<List<BookItem>> viewAllBooks(HttpServletRequest httpServletRequest)
    {
        boolean requestSuccess = false;
        ResponseEntity<List<BookItem>> response;
        int num_books = 0;
        Instant start = Instant.now();

        try
        {
            response = viewCatalogService.listAllBooks();
            num_books = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(requestSuccess)
                message = "All books have been listed. (# Books = " + num_books + ")";

            else
                message = "No books are available.";

            catalogLog(httpServletRequest.getRequestURL().toString(), message,
                    time);
        }
    }

    /*
     * All libraries viewing GET request.
     * Returns a 200 response code with all the libraries within the system.
     * Will return no libraries and a 404 response if there are no libraries found within the system.
     */
    @GetMapping("/library")
    public ResponseEntity<List<Library>> viewAllLibraries(HttpServletRequest httpServletRequest)
    {
        boolean requestSuccess = false;
        ResponseEntity<List<Library>> response;
        int num_libraries = 0;
        Instant start = Instant.now();

        try
        {
            response = viewCatalogService.listAllLibraries();
            num_libraries = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(requestSuccess)
                message = "All libraries have been listed. (# Libraries = " + num_libraries + ")";

            else
                message = "No libraries are available.";

            catalogLog(httpServletRequest.getRequestURL().toString(), message,
                    time);
        }
    }

    /*
     * All subjects viewing GET request.
     * Returns a 200 response code with all the subjects within the system.
     * Will return no subjects and a 404 response if there are no subjects found within the system.
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> viewAllSubjects(HttpServletRequest httpServletRequest)
    {
        boolean requestSuccess = false;
        ResponseEntity<List<Subject>> response;
        int num_subjects = 0;
        Instant start = Instant.now();

        try
        {
            response = viewCatalogService.listAllSubjects();
            num_subjects = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(requestSuccess)
                message = "All subjects have been listed. (# Subjects = " + num_subjects + ")";

            else
                message = "No subjects are available.";

            catalogLog(httpServletRequest.getRequestURL().toString(), message,
                    time);
        }
    }

    /*
     * All subjects viewing GET request.
     * Returns a 200 response code with all the subjects within the system.
     * Will return no authors and a 404 response if there are no authors found within the system.
     */
    @GetMapping("/author")
    public ResponseEntity<List<Author>> viewAllAuthors(HttpServletRequest httpServletRequest)
    {
        boolean requestSuccess = false;
        ResponseEntity<List<Author>> response;
        int num_authors = 0;
        Instant start = Instant.now();

        try
        {
            response = viewCatalogService.listAllAuthors();
            num_authors = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;

            if(requestSuccess)
                message = "All authors have been listed. (# Authors = " + num_authors + ")";

            else
                message = "No authors are available.";

            catalogLog(httpServletRequest.getRequestURL().toString(), message,
                    time);
        }
    }

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
    public ResponseEntity<List<BookItem>> searchBooks(HttpServletRequest httpServletRequest,
                                                      @RequestParam(value = "library", required = false, defaultValue = "none") String library,
                                                      @RequestParam(value = "title", required = false, defaultValue = "none") String title,
                                                      @RequestParam(value = "author", required = false, defaultValue = "none") String author,
                                                      @RequestParam(value = "subjects", required = false, defaultValue = "none") List<String> subjects,
                                                      @RequestParam(value = "pub_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date publicationDate)
    {
        boolean requestSuccess = false;
        int searchResults = 0;
        Instant start = Instant.now();

        try
        {
            ResponseEntity<List<BookItem>> response = viewCatalogService.searchBooks(
                     library, title, author, subjects, publicationDate);
            searchResults = response.getBody().size();
            requestSuccess = true;
            return response;
        }

        finally
        {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            String message;
            String pubDateLog;

            if(requestSuccess)
                message = searchResults + " books were found under this search.";

            else
                message = "No books are available under this search.";

            if(publicationDate != null)
                pubDateLog = publicationDate.toString();

            else
                pubDateLog = "none";

            catalogSearchLog(httpServletRequest.getRequestURL().toString(), message,
                    library, title, author, subjects.toString(), pubDateLog,
                    time);
        }
    }

    private void catalogLog(String requestURL, String message, long time)
    {
        String requestType = "GET";
        String successLog = "(Success! Completed in " + time + " ms)";

        log.info(requestType + " " + requestURL + " " + message + " " +
                successLog);
    }

    private void catalogSearchLog(String requestURL, String message, String library,
                                  String title, String author, String subjects,
                                  String pub_date, long time)
    {
        String requestType = "GET";
        String successLog = "(Success! Completed in " + time + " ms)";
        String search = "(Search: " +
                "Library = " + library + ", " +
                "Title = " + title + ", " +
                "Author = " + author + ", " +
                "Subjects = " + subjects + ", " +
                "Pub_Date = " + pub_date +
                ")";

        log.info(requestType + " " + requestURL + " " + message + " " +
                search + " " + successLog);
    }
}
