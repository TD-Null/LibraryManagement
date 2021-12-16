package com.example.LibraryManagement.models.accounts;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class LibraryCard
{
    private String cardNumber;
    private String barcode;
    private Date issuedAt;
    private boolean active;
}
