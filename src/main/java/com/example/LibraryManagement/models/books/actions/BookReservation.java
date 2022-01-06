package com.example.LibraryManagement.models.books.actions;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.enums.reservations.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class BookReservation
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "barcode", nullable = false)
    private BookItem bookItem;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "CreationDate", nullable = false)
    private Date creationDate;

    @Column(name = "Status", nullable = false)
    private ReservationStatus status;

    public BookReservation(BookItem bookItem, Member member, Date creationDate, ReservationStatus status)
    {
        this.bookItem = bookItem;
        this.member = member;
        this.creationDate = creationDate;
        this.status = status;
    }
}
