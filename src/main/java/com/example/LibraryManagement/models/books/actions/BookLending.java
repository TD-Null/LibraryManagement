package com.example.LibraryManagement.models.books.actions;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
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
}
