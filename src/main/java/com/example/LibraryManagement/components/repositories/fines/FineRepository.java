package com.example.LibraryManagement.components.repositories.fines;

import com.example.LibraryManagement.models.books.fines.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
}
