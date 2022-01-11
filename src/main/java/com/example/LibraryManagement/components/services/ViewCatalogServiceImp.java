package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.books.AuthorRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.components.repositories.books.LibraryRepository;
import com.example.LibraryManagement.components.repositories.books.SubjectRepository;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.interfaces.services.catalogs.ViewCatalogService;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

@AllArgsConstructor
@Service
public class ViewCatalogServiceImp implements ViewCatalogService
{
    @Autowired
    private final BookItemRepository bookItemRepository;
    @Autowired
    private final LibraryRepository libraryRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private final ValidationService validationService;

    public ResponseEntity<List<Library>> listAllLibraries() { return ResponseEntity.ok(libraryRepository.findAll()); }

    public ResponseEntity<List<BookItem>> listLibraryBooks(String name)
    {
        Library library = validationService.libraryValidation(name);

        Set<BookItem> libraryBooks = library.getBooks();
        List<BookItem> currBooks = new ArrayList<>(libraryBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books currently available in this library.",
                    HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> listAllBooks() { return ResponseEntity.ok(bookItemRepository.findAll()); }

    public ResponseEntity<List<Subject>> listAllSubjects() { return ResponseEntity.ok(subjectRepository.findAll()); }

    public ResponseEntity<List<Author>> listAllAuthors() { return ResponseEntity.ok(authorRepository.findAll()); }

    public ResponseEntity<List<BookItem>> searchBooks(String title, String author, List<String> subjects, Date publicationDate)
    {
        List<BookItem> books = new ArrayList<>();

        if(!title.equals("none"))
        {
            List<BookItem> booksByTitle = bookItemRepository.findAllByTitleContaining(title);

            for(BookItem b: booksByTitle)
                if(!books.contains(b)) books.add(b);
        }

        if(!author.equals("none"))
        {
            Author a = validationService.authorValidation(author);
            Set<BookItem> booksByAuthor = a.getBooks();

            for(BookItem b: booksByAuthor)
                if(!books.contains(b)) books.add(b);
        }

        if(subjects.contains("none"))
        {
            for(String name: subjects)
            {
                Subject subject = validationService.subjectValidation(name);
                Set<BookItem> booksBySubject = subject.getBooks();

                for(BookItem b: booksBySubject)
                    if(!books.contains(b)) books.add(b);
            }
        }

        if(publicationDate != null)
        {
            List<BookItem> booksByPubDate = bookItemRepository.findAllByPublicationDate(publicationDate);

            for(BookItem b: booksByPubDate)
                if(!books.contains(b)) books.add(b);
        }

        if(books.isEmpty())
            throw new ApiRequestException("There are no books found under this search.",
                    HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(books);
    }

    public ResponseEntity<List<BookItem>> searchBooksByTitle(String title)
    {
        List<BookItem> currBooks = bookItemRepository.findAllByTitleContaining(title);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books available under this title.",
                    HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksByAuthor(String name)
    {
        Author author = validationService.authorValidation(name);

        Set<BookItem> authorBooks = author.getBooks();
        List<BookItem> currBooks = new ArrayList<>(authorBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("The author does not have any books available in this library.",
                    HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksBySubject(String name)
    {
        Subject subject = validationService.subjectValidation(name);

        Set<BookItem> subjectBooks = subject.getBooks();
        List<BookItem> currBooks = new ArrayList<>(subjectBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books available under this subject.",
                    HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksByPubDate(Date publicationDate)
    {
        List<BookItem> currBooks = bookItemRepository.findAllByPublicationDate(publicationDate);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books available within this publication date.",
                    HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(currBooks);
    }
}
