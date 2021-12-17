package com.example.LibraryManagement.models.datatypes;

import lombok.Data;

@Data
public class Address
{
    private String streetAddress;
    private String city;
    private String zipcode;
    private String country;
}
