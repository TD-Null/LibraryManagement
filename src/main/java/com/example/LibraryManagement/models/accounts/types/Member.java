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

/*
 * Description:
 * All members can search the catalog, as well as check-out, reserve, renew,
 * and return a book.
 *
 * There should be a maximum limit (5) on how many books a member can check-out,
 * and there should be a maximum limit (10) on how many days a member can keep a
 * book.
 */
@Data
@Entity
@Table
public class Member extends Account
{
    @NotBlank
    @Column(name = "Date of Membership")
    private Date dateOfMembership;

    @Column(name = "Check Out Total")
    private int checkOutTotal;

    @Column(name = "Reserved Total")
    private int reservedTotal;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Book> checkedOutBooks = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Book> reservedBooks = new HashSet<>();
}
