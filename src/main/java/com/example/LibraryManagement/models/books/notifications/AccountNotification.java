package com.example.LibraryManagement.models.books.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Member;
import java.sql.Date;

@Data
@Entity
@Table
public class AccountNotification
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "Date", nullable = false)
    private Date createdOn;

    @Column(name = "Email")
    private String email;

    @Column(name = "Address")
    private String address;

    @NotBlank
    @Column(name = "Content")
    private String content;
}
