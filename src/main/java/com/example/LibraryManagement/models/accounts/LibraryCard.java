package com.example.LibraryManagement.models.accounts;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/*
 * Description:
 * Each library user will be issued a library card, which will
 * be used to identify users while issuing or returning books.
 *
 * Each account will have a single library card and will be
 * used as an additional form of logging in.
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "Card_Number")
})
public class LibraryCard
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barcode")
    private Long barcode;

    @JsonIgnore
    @OneToOne(mappedBy = "libraryCard")
    private Member member;

    @JsonIgnore
    @OneToOne(mappedBy = "libraryCard")
    private Librarian librarian;

    @Column(name = "Type", nullable = false)
    private AccountType type;

    @NotBlank
    @Size(min = 6, max = 6)
    @Column(name = "Card_Number")
    private String cardNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "Issue_Date", nullable = false)
    private Date issuedAt;

    @Column(name = "Active_Status")
    private boolean active;

    public LibraryCard(AccountType type, String cardNumber, Date issuedAt, boolean active)
    {
        this.type = type;
        this.cardNumber = cardNumber;
        this.issuedAt = issuedAt;
        this.active = active;
    }
}
