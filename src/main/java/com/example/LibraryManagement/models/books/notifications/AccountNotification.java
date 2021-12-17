package com.example.LibraryManagement.models.books.notifications;

import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.reservations.BookLending;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@MappedSuperclass
public class AccountNotification
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int notificationId;

    @NotBlank
    @Column(name = "Creation Date")
    private Date createdOn;

    @NotBlank
    @Column(name = "Content")
    private String content;

    @NotBlank
    private BookLending bookLending;

    @NotBlank
    private Fine fine;
}
