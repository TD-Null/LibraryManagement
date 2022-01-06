package com.example.LibraryManagement.components.repositories.books.libraries;

import com.example.LibraryManagement.models.books.libraries.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<Library, String> {
}
