package com.example.LibraryManagement.models.accounts;

import com.example.LibraryManagement.models.books.properties.Book;
import com.example.LibraryManagement.models.datatypes.Person;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
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
}
