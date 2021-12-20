package com.example.LibraryManagement.models.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Member;
import java.sql.Date;

/*
 * Description:
 * Each library user will be issued a library card, which will
 * be used to identify users while issuing or returning books.
 *
 * Each account will have a single library card and will be
 * used as an additional form of logging in.
 */
@Data
@Entity
@Table
public class LibraryCard
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barcode")
    private String barcode;

    @JsonIgnore
    @OneToOne(mappedBy = "libraryCard")
    private Account account;

    @NotBlank
    @Column(name = "Card_Number")
    private String cardNumber;

    @Column(name = "Issue_Date", nullable = false)
    private Date issuedAt;

    @Column(name = "Active_Status")
    private boolean active;
}
