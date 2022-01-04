package com.example.LibraryManagement.models.books.actions;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.enums.reservations.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Table
@Entity
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

    @Column(name = "Creation_Date", nullable = false)
    private Date creationDate;

    @Column(name = "Status", nullable = false)
    private ReservationStatus status;
}
