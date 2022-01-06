package com.example.LibraryManagement.components.repositories.books;

import com.example.LibraryManagement.models.books.properties.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long>
{
    List<BookItem> findAllByTitleContaining(String title);
    List<BookItem> findAllByPublicationDate(Date publicationDate);
}
