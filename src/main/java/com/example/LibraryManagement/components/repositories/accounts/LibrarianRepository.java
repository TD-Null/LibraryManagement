package com.example.LibraryManagement.components.repositories.accounts;

import com.example.LibraryManagement.models.accounts.types.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long>
{
    Optional<Librarian> findLibrarianByEmail(String email);
}
