package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;

import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * Description:
 * Mainly responsible for adding and modifying books, book items, and users.
 * The Librarian can also issue, reserve, and return book items.
 */
@Entity
@Table
public class Librarian extends Account
{
}
