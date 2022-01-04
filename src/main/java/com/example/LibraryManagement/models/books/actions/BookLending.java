package com.example.LibraryManagement.models.books.actions;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@EqualsAndHashCode
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "Creation_Date", nullable = false)
    private Date creationDate;

    @Column(name = "Due_Date", nullable = false)
    private Date dueDate;

    @Column(name = "Return_Date", nullable = false)
    private Date returnDate;

    public BookLending(BookItem bookItem, Member member, Date creationDate, Date dueDate, Date returnDate)
    {
        this.bookItem = bookItem;
        this.member = member;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }
}
