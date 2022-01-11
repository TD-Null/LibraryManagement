package com.example.LibraryManagement.models.books.actions;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table
public class BookLending
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "barcode", nullable = false)
    private BookItem bookItem;

    @ManyToOne
    @JoinColumn(name = "member", nullable = false)
    private Member member;

    @Column(name = "CreationDate", nullable = false)
    private Date creationDate;

    @Column(name = "DueDate", nullable = false)
    private Date dueDate;

    @Column(name = "ReturnDate")
    private Date returnDate;

    public BookLending(BookItem bookItem, Member member, Date creationDate, Date dueDate)
    {
        this.bookItem = bookItem;
        this.member = member;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
    }
}
