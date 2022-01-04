package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.LibrarianRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.components.repositories.books.BookLendingRepository;
import com.example.LibraryManagement.components.repositories.books.BookReservationRepository;
import com.example.LibraryManagement.components.repositories.books.FineRepository;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.interfaces.services.accounts.LibrarianService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<List<Librarian>> listAllLibrarians() { return ResponseEntity.ok(librarianRepository.findAll()); }

    public ResponseEntity<List<Member>> listAllMembers() { return ResponseEntity.ok(memberRepository.findAll()); }

    public ResponseEntity<List<BookLending>> listAllBookLoans() { return ResponseEntity.ok(bookLendingRepository.findAll()); }

    public ResponseEntity<List<BookReservation>> listAllBookReservations() { return ResponseEntity.ok(bookReservationRepository.findAll()); }

    public ResponseEntity<List<Fine>> listAllFines() { return ResponseEntity.ok(fineRepository.findAll()); }
}
