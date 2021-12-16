package com.example.LibraryManagement.models.books.reservations;

import com.example.LibraryManagement.models.enums.reservations.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class BookReservation
{
    private Date creationDate;
    private ReservationStatus status;
}
