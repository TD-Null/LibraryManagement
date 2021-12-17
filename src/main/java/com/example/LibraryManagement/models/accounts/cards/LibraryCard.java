package com.example.LibraryManagement.models.accounts.cards;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
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
