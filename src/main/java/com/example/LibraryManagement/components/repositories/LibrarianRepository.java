package com.example.LibraryManagement.components.repositories;

import com.example.LibraryManagement.models.accounts.types.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrarianRepository extends JpaRepository<Librarian, String>
{
}
