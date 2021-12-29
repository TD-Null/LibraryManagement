package com.example.LibraryManagement.components.repositories.books;

import com.example.LibraryManagement.models.books.actions.BookLending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLendingRepository extends JpaRepository<BookLending, Long> {
}
