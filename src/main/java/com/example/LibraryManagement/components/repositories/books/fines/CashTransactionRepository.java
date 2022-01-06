package com.example.LibraryManagement.components.repositories.books.fines;

import com.example.LibraryManagement.models.books.fines.transactions.CashTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, Long> {
}
