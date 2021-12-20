package com.example.LibraryManagement.components.repositories.accounts;

import com.example.LibraryManagement.models.accounts.types.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, String>
{
}
