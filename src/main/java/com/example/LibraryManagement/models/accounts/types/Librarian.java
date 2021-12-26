package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.datatypes.Person;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/*
 * Description:
 * Mainly responsible for adding and modifying books, book items, and users.
 * The Librarian will have access to updating accounts on their status as well
 * as check the current status of book items and their lending and reservation
 * statuses.
 */
@Getter
@Setter
@Entity
@Table
public class Librarian extends Account
{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "barcode", nullable = false)
    private LibraryCard libraryCard;

    public Librarian(String password, AccountStatus status, Person details)
    {
        super(password, status, details);
    }
}
