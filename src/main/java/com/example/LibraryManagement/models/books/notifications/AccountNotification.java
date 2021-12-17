package com.example.LibraryManagement.models.books.notifications;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
@MappedSuperclass
public class AccountNotification
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    @NotBlank
    @Column(name = "Date")
    private Date createdOn;

    @NotBlank
    @Column(name = "Content")
    private String content;
}
