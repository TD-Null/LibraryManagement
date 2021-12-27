package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.ViewCatalogServiceImp;
import com.example.LibraryManagement.models.books.properties.BookItem;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping("library_website/catalog")
public class ViewCatalogController
{
    @Autowired
    private final ViewCatalogServiceImp viewCatalogService;

    @GetMapping("/books")
    public ResponseEntity<List<BookItem>> viewAllBooks()
    {
        return viewCatalogService.listAllBooks();
    }

    @GetMapping("/books/title")
    public ResponseEntity<List<BookItem>> viewBooksByTitle(@RequestParam String title)
    {
        return viewCatalogService.searchBooksByTitle(title);
    }

    @GetMapping("/books/author")
    public ResponseEntity<List<BookItem>> viewBooksByAuthor(@RequestParam String name)
    {
        return viewCatalogService.searchBooksByAuthor(name);
    }

    @GetMapping("/books/subject")
    public ResponseEntity<List<BookItem>> viewBooksBySubject(@RequestParam String subject)
    {
        return viewCatalogService.searchBooksBySubject(subject);
    }

    @GetMapping("/books/publication_date")
    public ResponseEntity<List<BookItem>> viewBooksByPubDate(@RequestParam Date publicationDate)
    {
        return viewCatalogService.searchBooksByPubDate(publicationDate);
    }

//    @GetMapping("/library")
//    public ResponseEntity<List<Library>> viewAllLibraries()
//    {
//
//    }
//
//    @GetMapping("/library/{name}/books")
//    public ResponseEntity<List<BookItem>> viewLibraryBooks(@PathVariable("name") String libraryName)
//    {
//
//    }
//
//
//
//    @GetMapping("/library/{name}/{number}/books")
//    public ResponseEntity<List<BookItem>> viewLibraryRackBooks(@PathVariable("name") String libraryName,
//                                                               @PathVariable("number") int rackNumber)
//    {
//
//    }
}
