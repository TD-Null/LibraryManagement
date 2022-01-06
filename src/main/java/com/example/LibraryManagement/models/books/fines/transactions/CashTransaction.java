package com.example.LibraryManagement.models.books.fines.transactions;

import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.books.fines.FineTransaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

// Type of transaction that uses cash to pay a fine.
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class CashTransaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @NotNull
    private double cashTendered;

    @JsonIgnore
    @OneToOne(mappedBy = "cashTransaction")
    private FineTransaction fineTransaction;

    public CashTransaction(double cashTendered) { this.cashTendered = cashTendered; }
}
