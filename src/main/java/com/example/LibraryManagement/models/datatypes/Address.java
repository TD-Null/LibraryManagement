package com.example.LibraryManagement.models.datatypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Address
{
    private String streetAddress;
    private String city;
    private String zipcode;
    private String country;
}
