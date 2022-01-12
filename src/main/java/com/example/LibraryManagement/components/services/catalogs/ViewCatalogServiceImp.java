package com.example.LibraryManagement.components.services.catalogs;

import com.example.LibraryManagement.components.repositories.books.AuthorRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.components.repositories.books.LibraryRepository;
import com.example.LibraryManagement.components.repositories.books.SubjectRepository;
import com.example.LibraryManagement.components.services.ValidationService;
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

    public ResponseEntity<List<Library>> listAllLibraries()
    {
        List<Library> libraries = libraryRepository.findAll();

        if(libraries.isEmpty())
            throw new ApiRequestException("No libraries available.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(libraries);
    }

    public ResponseEntity<List<Subject>> listAllSubjects()
    {
        List<Subject> subjects = subjectRepository.findAll();

        if(subjects.isEmpty())
            throw new ApiRequestException("No subjects available.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(subjects);
    }

    public ResponseEntity<List<Author>> listAllAuthors()
    {
        List<Author> authors = authorRepository.findAll();

        if(authors.isEmpty())
            throw new ApiRequestException("No authors available.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(authors);
    }

    public ResponseEntity<List<BookItem>> listAllBooks()
    {
        List<BookItem> books = bookItemRepository.findAll();

        if(books.isEmpty())
            throw new ApiRequestException("No books available.", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(books);
    }

    public ResponseEntity<List<BookItem>> searchBooks(String libraryName,
                                                      String title,
                                                      String authorName,
                                                      List<String> subjects,
                                                      Date publicationDate)
    {
        List<BookItem> books = new ArrayList<>();

        if(!libraryName.equals("none"))
        {
            Library library = validationService.libraryValidation(libraryName);
            Set<BookItem> libraryBooks = library.getBooks();

            for(BookItem b: libraryBooks)
                if(!books.contains(b)) books.add(b);
        }

        if(!title.equals("none"))
        {
            List<BookItem> booksByTitle = bookItemRepository.findAllByTitleContaining(title);

            for(BookItem b: booksByTitle)
                if(!books.contains(b)) books.add(b);
        }

        if(!authorName.equals("none"))
        {
            Author author = validationService.authorValidation(authorName);
            Set<BookItem> booksByAuthor = author.getBooks();

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
}
