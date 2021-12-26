package com.example.LibraryManagement.models.accounts;

import com.example.LibraryManagement.models.datatypes.Person;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id;

    @NotBlank
    @Size(min = 6, max = 40)
    @Column(name = "Password")
    private String password;

    @Column(name = "Status", nullable = false)
    private AccountStatus status;

    @Column(name = "Details", nullable = false)
    private Person details;

    public Account(String password, AccountStatus status, Person details)
    {
        this.password = password;
        this.status = status;
        this.details = details;
    }
}
