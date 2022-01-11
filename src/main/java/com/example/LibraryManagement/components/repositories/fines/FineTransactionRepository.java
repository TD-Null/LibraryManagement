package com.example.LibraryManagement.components.repositories.fines;

import com.example.LibraryManagement.models.books.fines.FineTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FineTransactionRepository extends JpaRepository<FineTransaction, Long> {
}
