package com.example.LibraryManagement.models.books.fines;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/*
 * Contains a table of fines records that can be associated with
 * users in the case of returning books past their due dates. Each
 * fine can be paid with a single transaction.
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class Fine
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Amount")
    private double amount;

    @Column(name = "Paid")
    private boolean paid;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fineTransaction")
    private FineTransaction fineTransaction;

    public Fine(double amount) { this.amount = amount; }
}
