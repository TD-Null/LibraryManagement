package com.example.LibraryManagement.models.accounts.cards;

import lombok.Data;

import java.sql.Date;

@Data
public class BarcodeReader
{
    private String id;
    private Date registeredAt;
    private boolean active;
}
