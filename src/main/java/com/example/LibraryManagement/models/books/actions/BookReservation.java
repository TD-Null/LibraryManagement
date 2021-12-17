package com.example.LibraryManagement.models.books.actions;

import com.example.LibraryManagement.models.enums.reservations.ReservationStatus;
import lombok.Data;

import java.sql.Date;

@Data
public class BookReservation
{
    private Date creationDate;
    private ReservationStatus status;
}
