package com.example.LibraryManagement.components.repositories.books;

import com.example.LibraryManagement.models.books.actions.BookReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
}
