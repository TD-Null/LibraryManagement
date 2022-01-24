package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.repositories.books.AuthorRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.components.repositories.books.SubjectRepository;
import com.example.LibraryManagement.components.repositories.fines.FineRepository;
import com.example.LibraryManagement.components.repositories.books.LibraryRepository;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Member;
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

import java.util.ArrayList;
import java.util.Optional;

/*
 * Service with reusable code primarily used for validation
 * within the repositories of the application.
 */
@AllArgsConstructor
@Service
public class ValidationService
{
    @Autowired
    private final LibraryCardRepository libraryCardRepository;
    @Autowired
    private final MemberRepository memberRepository;
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

    public LibraryCard cardNumberValidation(String cardNumber)
    {
        Optional<LibraryCard> card = libraryCardRepository.findLibraryCardByCardNumber(cardNumber);

        if(card.isEmpty())
            throw new ApiRequestException("Invalid credentials. " +
                    "(Wrong library card number or password)",
                    HttpStatus.UNAUTHORIZED);

        return card.get();
    }

    public Member memberValidation(Long memberID)
    {
        Optional<Member> member = memberRepository.findById(memberID);

        if(member.isEmpty())
            throw new ApiRequestException("Unable to find member's account within the system.",
                    HttpStatus.NOT_FOUND);

        return member.get();
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
            throw new ApiRequestException("Unable to find this library within the system.",
                    HttpStatus.NOT_FOUND);

        return library.get();
    }

    public void addLibraryValidation(String library)
    {
        if(libraryRepository.existsById(library))
            throw new ApiRequestException("Library already exists within the system.",
                    HttpStatus.CONFLICT);
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
        Optional<Subject> subject = subjectRepository.findById(name);

        if(subject.isEmpty())
            throw new ApiRequestException("Subject does not exist within the system.",
                    HttpStatus.NOT_FOUND);

        return subject.get();
    }

    public void addSubjectValidation(String subject)
    {
        if(subjectRepository.existsById(subject))
            throw new ApiRequestException("Subject already exists within the system.",
                    HttpStatus.CONFLICT);
    }

    public Subject addBookSubjectValidation(String subject)
    {
        if(!subjectRepository.existsById(subject))
            subjectRepository.save(new Subject(subject));

        return subjectRepository.getById(subject);
    }

    public Author authorValidation(String name)
    {
        Optional<Author> author = authorRepository.findById(name);

        if(author.isEmpty())
            throw new ApiRequestException("Author does not exist within the system.",
                    HttpStatus.NOT_FOUND);

        return author.get();
    }

    public void addAuthorValidation(String author)
    {
        if(authorRepository.existsById(author))
            throw new ApiRequestException("Author already exists within the system.",
                    HttpStatus.CONFLICT);
    }

    public Author addBookAuthorValidation(String author)
    {
        if(!authorRepository.existsById(author))
            authorRepository.save(new Author(author));

        return authorRepository.getById(author);
    }
}
