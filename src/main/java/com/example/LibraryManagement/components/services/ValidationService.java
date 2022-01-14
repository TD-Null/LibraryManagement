package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.books.AuthorRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.components.repositories.books.SubjectRepository;
import com.example.LibraryManagement.components.repositories.fines.FineRepository;
import com.example.LibraryManagement.components.repositories.books.LibraryRepository;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ValidationService
{
    @Autowired
    private final LibraryCardRepository libraryCardRepository;
    @Autowired
    private final FineRepository fineRepository;
    @Autowired
    private final BookItemRepository bookItemRepository;
    @Autowired
    private final LibraryRepository libraryRepository;
    @Autowired
    private final SubjectRepository subjectRepository;
    @Autowired
    private final AuthorRepository authorRepository;

    public LibraryCard cardValidation(Long barcode)
    {
        Optional<LibraryCard> card = libraryCardRepository.findById(barcode);

        if(card.isEmpty())
            throw new ApiRequestException("Unable to find library card within the system.",
                    HttpStatus.UNAUTHORIZED);

        return card.get();
    }

    public Fine fineValidation(Long ID)
    {
        Optional<Fine> fine = fineRepository.findById(ID);

        if(fine.isEmpty())
            throw new ApiRequestException("Unable to find fine within the system.",
                    HttpStatus.NOT_FOUND);

        return fine.get();
    }

    public Library libraryValidation(String name)
    {
        Optional<Library> library = libraryRepository.findById(name);

        if(library.isEmpty())
            throw new ApiRequestException("Unable to find this library.",
                    HttpStatus.NOT_FOUND);

        return library.get();
    }

    public BookItem bookValidation(Long barcode)
    {
        Optional<BookItem> bookItem = bookItemRepository.findById(barcode);

        if(bookItem.isEmpty())
            throw new ApiRequestException("Unable to find book within the system.",
                    HttpStatus.NOT_FOUND);

        return bookItem.get();
    }

    public Subject subjectValidation(String name)
    {
        if(!subjectRepository.existsById(name))
        {
            subjectRepository.save(new Subject(name));
        }

        return subjectRepository.getById(name);
    }

    public Author authorValidation(String name)
    {
        if(!authorRepository.existsById(name))
        {
            authorRepository.save(new Author(name));
        }

        return authorRepository.getById(name);
    }
}
