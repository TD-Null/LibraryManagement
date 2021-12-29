package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.LibrarianRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.repositories.books.BookItemRepository;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
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

    public ResponseEntity<List<Librarian>> listAllLibrarians() { return ResponseEntity.ok(librarianRepository.findAll()); }

    public ResponseEntity<List<Member>> listAllMembers() { return ResponseEntity.ok(memberRepository.findAll()); }

}
