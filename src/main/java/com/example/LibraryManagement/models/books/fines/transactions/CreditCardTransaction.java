package com.example.LibraryManagement.models.books.fines.transactions;

import com.example.LibraryManagement.models.books.fines.FineTransaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

// Type of transaction that uses a credit card to pay a fine.
@Getter
@Setter
@EqualsAndHashCode(exclude = "fineTransaction")
@NoArgsConstructor
@Entity
@Table
public class CreditCardTransaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    private String nameOnCard;

    @JsonIgnore
    @OneToOne(mappedBy = "creditCardTransaction")
    private FineTransaction fineTransaction;

    public CreditCardTransaction(String nameOnCard) { this.nameOnCard = nameOnCard; }
}
