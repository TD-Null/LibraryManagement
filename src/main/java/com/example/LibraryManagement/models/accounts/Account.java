package com.example.LibraryManagement.models.accounts;

import com.example.LibraryManagement.models.datatypes.Person;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@MappedSuperclass
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id;

    @NotBlank
    @Column(name = "Password")
    private String password;

    @NotBlank
    @Column(name = "Status")
    private AccountStatus status;

    @NotBlank
    @Column(name = "Details")
    private Person details;
}
