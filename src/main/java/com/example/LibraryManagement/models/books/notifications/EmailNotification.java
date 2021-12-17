package com.example.LibraryManagement.models.books.notifications;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table
public class EmailNotification
{
    @NotBlank
    @Column(name = "Email")
    private String email;
}
