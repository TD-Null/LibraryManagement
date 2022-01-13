package com.example.LibraryManagement.components.services.accounts;

import com.example.LibraryManagement.components.repositories.accounts.LibrarianRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.repositories.books.BookLendingRepository;
import com.example.LibraryManagement.components.repositories.books.BookReservationRepository;
import com.example.LibraryManagement.components.repositories.fines.FineRepository;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.interfaces.services.accounts.LibrarianService;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LibrarianServiceImp implements LibrarianService
{
    @Autowired
    private final LibrarianRepository librarianRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final BookLendingRepository bookLendingRepository;
    @Autowired
    private final BookReservationRepository bookReservationRepository;
    @Autowired
    private final FineRepository fineRepository;

    public ResponseEntity<List<Librarian>> listAllLibrarians()
    {
        List<Librarian> librarians = librarianRepository.findAll();

        if(librarians.isEmpty())
            throw new ApiRequestException("There are no librarians within the system.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(librarians);
    }

    public ResponseEntity<List<Member>> listAllMembers()
    {
        List<Member> members = memberRepository.findAll();

        if(members.isEmpty())
            throw new ApiRequestException("There are no members within the system.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(members);
    }

    public ResponseEntity<List<BookLending>> listAllBookLoans()
    {
        List<BookLending> bookLoans = bookLendingRepository.findAll();

        if(bookLoans.isEmpty())
            throw new ApiRequestException("There are no book loans made within the system.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(bookLoans);
    }

    public ResponseEntity<List<BookReservation>> listAllBookReservations()
    {
        List<BookReservation> reservations = bookReservationRepository.findAll();

        if(reservations.isEmpty())
            throw new ApiRequestException("There are no reservations made within the system.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(bookReservationRepository.findAll());
    }

    public ResponseEntity<List<Fine>> listAllFines()
    {
        List<Fine> fines = fineRepository.findAll();

        if(fines.isEmpty())
            throw new ApiRequestException("There are no fines made within the system.", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(fineRepository.findAll());
    }
}
