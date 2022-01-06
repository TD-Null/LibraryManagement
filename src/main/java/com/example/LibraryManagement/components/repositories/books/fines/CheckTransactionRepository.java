package com.example.LibraryManagement.components.repositories.books.fines;

import com.example.LibraryManagement.models.books.fines.transactions.CheckTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckTransactionRepository extends JpaRepository<CheckTransaction, Long> {
}
