package com.example.LibraryManagement.models.accounts.types;

import com.example.LibraryManagement.models.accounts.Account;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
@Entity
@Table
public class Member extends Account
{
    @NotBlank
    @Column(name = "Date of Membership")
    private Date dateOfMembership;
}
