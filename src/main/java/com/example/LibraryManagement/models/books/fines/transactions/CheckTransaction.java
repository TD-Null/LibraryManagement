package com.example.LibraryManagement.models.books.fines.transactions;

import com.example.LibraryManagement.models.books.fines.FineTransaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

// Type of transaction that uses a check to pay a fine.
@Getter
@Setter
@EqualsAndHashCode(exclude = "fineTransaction")
@NoArgsConstructor
@Entity
@Table
public class CheckTransaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    private String bankName;

    @NotBlank
    private String checkNumber;

    @JsonIgnore
    @OneToOne(mappedBy = "checkTransaction")
    private FineTransaction fineTransaction;

    public CheckTransaction(String bankName, String checkNumber)
    {
        this.bankName = bankName;
        this.checkNumber = checkNumber;
    }
}
