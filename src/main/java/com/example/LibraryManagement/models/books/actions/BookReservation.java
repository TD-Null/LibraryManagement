package com.example.LibraryManagement.models.books.actions;

import com.example.LibraryManagement.models.enums.reservations.ReservationStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Data
@Table
@Entity
public class BookReservation
{
    private Date creationDate;
    private ReservationStatus status;
}
