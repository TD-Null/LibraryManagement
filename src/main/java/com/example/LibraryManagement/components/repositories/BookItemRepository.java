package com.example.LibraryManagement.components.repositories;

import com.example.LibraryManagement.models.books.properties.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookItemRepository extends JpaRepository<BookItem, String>
{
}
