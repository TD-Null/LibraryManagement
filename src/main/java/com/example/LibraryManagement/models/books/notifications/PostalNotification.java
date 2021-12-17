package com.example.LibraryManagement.models.books.notifications;

import com.example.LibraryManagement.models.datatypes.Address;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table
public class PostalNotification
{
    @NotBlank
    @Column(name = "Address")
    private Address address;
}
