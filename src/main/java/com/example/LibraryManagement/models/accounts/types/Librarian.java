package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import com.example.LibraryManagement.models.accounts.LibraryCard;

import javax.persistence.*;

/*
 * Description:
 * Mainly responsible for adding and modifying books, book items, and users.
 * The Librarian will have access to updating accounts on their status as well
 * as check the current status of book items and their lending and reservation
 * statuses.
 */
@Entity
@Table
public class Librarian extends Account
{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "barcode", nullable = false)
    private LibraryCard libraryCard;
}
