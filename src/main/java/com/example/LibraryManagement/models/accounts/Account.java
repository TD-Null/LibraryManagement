package com.example.LibraryManagement.models.accounts;

import com.example.LibraryManagement.models.accounts.cards.LibraryCard;
import com.example.LibraryManagement.models.books.properties.Book;
import com.example.LibraryManagement.models.datatypes.Person;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id;

    @NotBlank
    @Column(name = "Password")
    private String password;

    @NotBlank
    @Column(name = "Status")
    private AccountStatus status;

    @NotBlank
    @Column(name = "Details")
    private Person person;

    @NotBlank
    @Column(name = "Library Card")
    private LibraryCard libraryCard;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Book> checkedOutBooks = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Book> reservedBooks = new HashSet<>();
}
