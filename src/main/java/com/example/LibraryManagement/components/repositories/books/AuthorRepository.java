package com.example.LibraryManagement.components.repositories.books;

import com.example.LibraryManagement.models.books.properties.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, String> {
}
