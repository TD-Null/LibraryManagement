package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.books.properties.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
public class Member extends Account
{
    @NotBlank
    @Column(name = "Date of Membership")
    private Date dateOfMembership;

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
