package com.example.LibraryManagement.models.books.actions;

import com.example.LibraryManagement.models.books.libraries.Library;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.books.properties.BookItem;
import lombok.Data;

import javax.persistence.*;
import java.lang.reflect.Member;
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
    @JoinColumn(name = "book_item_id", nullable = false)
    private BookItem bookItem;

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
