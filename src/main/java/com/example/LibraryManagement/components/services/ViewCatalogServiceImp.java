package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.books.AuthorRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.components.repositories.books.SubjectRepository;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.interfaces.services.ViewCatalogService;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@AllArgsConstructor
@Service
public class ViewCatalogServiceImp implements ViewCatalogService
{
    @Autowired
    private final BookItemRepository bookItemRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    @Autowired
    private final SubjectRepository subjectRepository;

    public ResponseEntity<List<BookItem>> listAllBooks() { return ResponseEntity.ok(bookItemRepository.findAll()); }

    public ResponseEntity<List<BookItem>> searchBooksByTitle(String title)
    {
        return null;
    }

    public ResponseEntity<List<BookItem>> searchBooksByAuthor(String name)
    {
        Optional<Author> author = authorRepository.findById(name);

        if(author.isEmpty())
        {
            throw new ApiRequestException("Unable to find books by author's name.");
        }

        Set<BookItem> authorBooks = author.get().getBooks();
        ArrayList<BookItem> currBooks = new ArrayList<>();
        currBooks.addAll(authorBooks);

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksBySubject(String subject)
    {
        Set<BookItem> subjectBooks = subjectRepository.getById(subject).getBooks();
        List<BookItem> currBooks = new ArrayList<>();
        currBooks.addAll(subjectBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("Unable to find books by the given subject " + subject);

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksByPubDate(Date publicationDate) {
        return null;
    }
}
