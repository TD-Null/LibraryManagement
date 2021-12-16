package com.example.LibraryManagement.models.accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class BarcodeReader
{
    private String id;
    private Date registeredAt;
    private boolean active;
}
