package com.example.LibraryManagement.models.accounts;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
@Entity
@Table
public class LibraryCard
{
    @NotBlank
    private String cardNumber;

    @NotBlank
    private String barcode;

    @NotBlank
    private Date issuedAt;

    private boolean active;
}
