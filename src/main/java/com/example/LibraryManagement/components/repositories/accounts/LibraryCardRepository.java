package com.example.LibraryManagement.components.repositories.accounts;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCard, Long>
{
    Optional<LibraryCard> findLibraryCardByCardNumber(String cardNumber);
}
