package com.example.LibraryManagement.components.repositories;

import com.example.LibraryManagement.models.books.properties.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String>
{
}
