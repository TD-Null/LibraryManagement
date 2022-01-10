package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.books.AuthorRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.components.repositories.books.SubjectRepository;
import com.example.LibraryManagement.components.repositories.books.fines.FineRepository;
import com.example.LibraryManagement.components.repositories.books.libraries.LibraryRepository;
import com.example.LibraryManagement.components.repositories.books.libraries.RackRepository;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.books.properties.Author;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
    private final RackRepository rackRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final SubjectRepository subjectRepository;

    public LibraryCard cardValidation(Long barcode)
    {
        Optional<LibraryCard> card = libraryCardRepository.findById(barcode);

        if(card.isEmpty())
            throw new ApiRequestException("Unable to find library card within the system.",
                    HttpStatus.UNAUTHORIZED);

        return card.get();
    }

    private Fine fineValidation(Long ID)
    {
        Optional<Fine> fine = fineRepository.findById(ID);

        if(fine.isEmpty())
            throw new ApiRequestException("Unable to find fine within the system.", HttpStatus.BAD_REQUEST);

        return fine.get();
    }

    private Library libraryValidation(String name)
    {
        Optional<Library> library = libraryRepository.findById(name);

        if(library.isEmpty())
            throw new ApiRequestException("Unable to find this library.",
                    HttpStatus.BAD_REQUEST);

        return library.get();
    }

    public Rack rackValidation(Library library, long rackID)
    {
        if(!rackRepository.existsById(rackID))
            throw new ApiRequestException("This rack does not exist within the system",
                    HttpStatus.BAD_REQUEST);

        Rack rack = rackRepository.getById(rackID);

        Set<Rack> libraryRacks = library.getRacks();

        if(libraryRacks.isEmpty())
            throw new ApiRequestException("There are no racks available in this library",
                    HttpStatus.BAD_REQUEST);

        else if(!libraryRacks.contains(rack))
            throw new ApiRequestException("This rack is not present within this library.",
                    HttpStatus.BAD_REQUEST);

        return rack;
    }

    public BookItem bookValidation(Long barcode)
    {
        Optional<BookItem> bookItem = bookItemRepository.findById(barcode);

        if(bookItem.isEmpty())
            throw new ApiRequestException("Unable to find book within the system.",
                    HttpStatus.BAD_REQUEST);

        return bookItem.get();
    }

    public Author authorValidation(String name)
    {
        if(!authorRepository.existsById(name))
        {
            authorRepository.save(new Author(name));
        }

        return authorRepository.getById(name);
    }

    public Subject subjectValidation(String name)
    {
        if(!subjectRepository.existsById(name))
        {
            subjectRepository.save(new Subject(name));
        }

        return subjectRepository.getById(name);
    }
}
