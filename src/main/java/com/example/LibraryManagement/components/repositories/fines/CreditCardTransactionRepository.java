package com.example.LibraryManagement.components.repositories.fines;

import com.example.LibraryManagement.models.books.fines.transactions.CreditCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long> {
}
