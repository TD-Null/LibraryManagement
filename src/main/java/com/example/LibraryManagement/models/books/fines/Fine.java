package com.example.LibraryManagement.models.books.fines;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Entity
@Table
public class Fine
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Amount")
    private double amount;

    @Column(name = "Paid")
    private boolean paid;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fineTransaction")
    private FineTransaction fineTransaction;

    public Fine(double amount, Member member)
    {
        this.amount = amount;
        this.member = member;
    }
}
