package com.example.LibraryManagement.models.books.notifications;

import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.datatypes.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
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
    private Address address;

    @NotBlank
    @Column(name = "Content")
    private String content;

    public AccountNotification(Date createdOn, String email, Address address, String content)
    {
        this.createdOn = createdOn;
        this.email = email;
        this.address = address;
        this.content = content;
    }
}
