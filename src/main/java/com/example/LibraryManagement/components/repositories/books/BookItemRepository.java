package com.example.LibraryManagement.components.repositories.books;

import com.example.LibraryManagement.models.books.properties.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, String>
{
}
