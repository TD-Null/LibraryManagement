package com.example.LibraryManagement.models.accounts;

import com.example.LibraryManagement.models.datatypes.Person;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/*
 * Description:
 * There will be two types of accounts, a librarian and a member.
 *
 * Librarians will have access to adding and modifying book items,
 * while members will have access to borrowing, renewing, and
 * returning book items from a library.
 *
 * Each account will have a library card, a password, a status,
 * and the personal details of the user.
 */
@Data
@MappedSuperclass
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "barcode", nullable = false)
    private LibraryCard libraryCard;

    @NotBlank
    @Column(name = "Password")
    private String password;

    @Column(name = "Status", nullable = false)
    private AccountStatus status;

    @Column(name = "Details", nullable = false)
    private Person details;
}
