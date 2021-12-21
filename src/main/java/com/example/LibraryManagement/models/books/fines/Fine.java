package com.example.LibraryManagement.models.books.fines;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.lang.reflect.Member;

/*
 * Contains a table of fines records that can be associated with
 * users in the case of returning books past their due dates. Each
 * fine can be paid with a single transaction.
 */
@Data
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
    @JoinColumn(name = "fineTransaction_id")
    private FineTransaction fineTransaction;
}
