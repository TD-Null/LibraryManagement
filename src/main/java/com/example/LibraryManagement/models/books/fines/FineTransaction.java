package com.example.LibraryManagement.models.books.fines;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.books.fines.transactions.CashTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CheckTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CreditCardTransaction;
import com.example.LibraryManagement.models.enums.fines.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/*
 * Contains a table of records of transactions made by users to
 * pay their fines. The transaction can either be by credit card,
 * check, or cash. The amount paid in the transaction must be at
 * least the value of the fine the user must pay.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "fine")
@NoArgsConstructor
@Entity
@Table
public class FineTransaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "fineTransaction")
    private Fine fine;

    @Column(name = "Type", nullable = false)
    private TransactionType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creditCardTransaction")
    private CreditCardTransaction creditCardTransaction;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "checkTransaction")
    private CheckTransaction checkTransaction;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cashTransaction")
    private CashTransaction cashTransaction;

    @Temporal(TemporalType.DATE)
    @Column(name = "Creation_Date", nullable = false)
    private Date creationDate;

    @Column(name = "Amount", nullable = false)
    private double amount;

    public FineTransaction(TransactionType type, Date creationDate, double amount)
    {
        this.type = type;
        this.creationDate = creationDate;
        this.amount = amount;
    }
}
