package com.example.LibraryManagement.models.books.fines;

import com.example.LibraryManagement.models.enums.fines.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

/*
 * Contains a table of records of transactions made by users to
 * pay their fines. The transaction can either be by credit card,
 * check, or cash. The amount paid in the transaction must be at
 * least the value of the fine the user must pay.
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class FineTransaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "fineTransaction")
    private Fine fine;

    @Column(name = "Type", nullable = false)
    private TransactionType type;

    @Column(name = "Transaction", nullable = false)
    private Object transaction;

    @Column(name = "Creation_Date", nullable = false)
    private Date creationDate;

    @Column(name = "Amount", nullable = false)
    private double amount;

    public FineTransaction(TransactionType type, Object transaction, Date creationDate, double amount)
    {
        this.type = type;
        this.transaction = transaction;
        this.creationDate = creationDate;
        this.amount = amount;
    }
}
