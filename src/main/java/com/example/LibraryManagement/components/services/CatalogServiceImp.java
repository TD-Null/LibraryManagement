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
import com.example.LibraryManagement.models.interfaces.services.catalogs.CatalogService;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@AllArgsConstructor
@Service
public class CatalogServiceImp implements CatalogService
{
    @Autowired
    private final BookItemRepository bookItemRepository;
    @Autowired
    private final LibraryRepository libraryRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final SubjectRepository subjectRepository;

    public ResponseEntity<List<BookItem>> listAllBooks() { return ResponseEntity.ok(bookItemRepository.findAll()); }

    public ResponseEntity<List<Library>> listAllLibraries() { return ResponseEntity.ok(libraryRepository.findAll()); }

    public ResponseEntity<List<BookItem>> listLibraryBooks(String name)
    {
        Optional<Library> library = libraryRepository.findById(name);

        if(library.isEmpty())
            throw new ApiRequestException("Unable to find this library.");

        Set<BookItem> libraryBooks = library.get().getBooks();
        List<BookItem> currBooks = new ArrayList<>(libraryBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books currently available in this library.");

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> listLibraryRackBooks(String name, int number)
    {
        Optional<Library> library = libraryRepository.findById(name);

        if(library.isEmpty())
            throw new ApiRequestException("Unable to find this library.");

        Set<Rack> libraryRacks = library.get().getRacks();

        if(libraryRacks.isEmpty())
            throw new ApiRequestException("There are no racks available in this library");

        Rack currRack = null;

        for(Rack r: libraryRacks)
        {
            if(r.getNumber() == number)
            {
                currRack = r;
                break;
            }
        }

        if(currRack == null)
            throw new ApiRequestException("This rack is not present within this library.");

        Set<BookItem> libraryBooks = library.get().getBooks();
        List<BookItem> currBooks = new ArrayList<>(libraryBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books available on this rack.");

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksByTitle(String title)
    {
        List<BookItem> currBooks = bookItemRepository.findAllByTitleContaining(title);

        if(currBooks.isEmpty())
            throw new ApiRequestException("There are no books available under this title.");

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksByAuthor(String name)
    {
        Optional<Author> author = authorRepository.findById(name);

        if(author.isEmpty())
            throw new ApiRequestException("Unable to find books by author's name.");

        Set<BookItem> authorBooks = author.get().getBooks();
        List<BookItem> currBooks = new ArrayList<>(authorBooks);

        if(currBooks.isEmpty())
            throw new ApiRequestException("The author does not have any books available in this library.");

        return ResponseEntity.ok(currBooks);
    }

    public ResponseEntity<List<BookItem>> searchBooksBySubject(String subjectName)
    {
        Optional<Subject> subject = subjectRepository.findById(subjectName);

        if(subject.isEmpty())
            throw new ApiRequestException("Unable to find books by author's name.");

        Set<BookItem> subjectBooks = subject.get().getBooks();
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
}
