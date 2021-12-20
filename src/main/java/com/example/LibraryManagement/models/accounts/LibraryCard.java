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
 * Each library member will have a single library card that is
 * linked to their account.
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
    private Member member;

    @NotBlank
    @Column(name = "Card Number")
    private String cardNumber;

    @NotBlank
    @Column(name = "Issue Date")
    private Date issuedAt;

    @Column(name = "Active Status")
    private boolean active;
}
