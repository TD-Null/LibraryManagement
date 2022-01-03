package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.books.AuthorRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.components.repositories.books.LibraryRepository;
import com.example.LibraryManagement.components.repositories.books.SubjectRepository;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import com.example.LibraryManagement.models.interfaces.services.catalogs.ViewCatalogService;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public ResponseEntity<List<Library>> listAllLibraries() { return ResponseEntity.ok(libraryRepository.findAll()); }

    public ResponseEntity<List<BookItem>> listLibraryBooks(String name)
    {
        Library library = libraryValidation(name);

        Set<BookItem> libraryBooks = library.getBooks();
        List<BookItem> currBooks = new ArrayList<>(libraryBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books currently available in this library.");

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> listAllBooks() { return ResponseEntity.ok(bookItemRepository.findAll()); }

    public ResponseEntity<List<Subject>> listAllSubjects() { return ResponseEntity.ok(subjectRepository.findAll()); }

    public ResponseEntity<List<Author>> listAllAuthors() { return ResponseEntity.ok(authorRepository.findAll()); }

    public ResponseEntity<List<BookItem>> searchBooksByTitle(String title)
    {
        List<BookItem> currBooks = bookItemRepository.findAllByTitleContaining(title);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books available under this title.");

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksByAuthor(String name)
    {
        Author author = authorValidation(name);

        Set<BookItem> authorBooks = author.getBooks();
        List<BookItem> currBooks = new ArrayList<>(authorBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("The author does not have any books available in this library.");

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksBySubject(String name)
    {
        Subject subject = subjectValidation(name);

        Set<BookItem> subjectBooks = subject.getBooks();
        List<BookItem> currBooks = new ArrayList<>(subjectBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books available under this subject.");

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksByPubDate(Date publicationDate)
    {
        List<BookItem> currBooks = bookItemRepository.findAllByPublicationDate(publicationDate);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books available within this publication date.");

        return ResponseEntity.ok(currBooks);
    }

    private Library libraryValidation(String name)
    {
        Optional<Library> library = libraryRepository.findById(name);

        if(library.isEmpty())
            throw new ApiRequestException("Unable to find this library.");

        return library.get();
    }

    private Author authorValidation(String name)
    {
        if(!authorRepository.existsById(name))
        {
            authorRepository.save(new Author(name));
        }

        return authorRepository.getById(name);
    }

    private Subject subjectValidation(String name)
    {
        if(!subjectRepository.existsById(name))
        {
            subjectRepository.save(new Subject(name));
        }

        return subjectRepository.getById(name);
    }
}
