package com.example.LibraryManagement.models.interfaces.services.accounts;

import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import org.springframework.http.ResponseEntity;

import java.util.List;

// Methods used in a service component relating to librarians.
public interface LibrarianService
{
    ResponseEntity<List<Librarian>> listAllLibrarians();

    ResponseEntity<List<Member>> listAllMembers();
}
