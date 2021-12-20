package com.example.LibraryManagement.models.datatypes;

import lombok.Data;

/*
 * Contains details regarding a user's/account's address,
 * including:
 *
 * Street Address
 * City
 * Zipcode
 * Country
 */
@Data
public class Address
{
    private String streetAddress;
    private String city;
    private String zipcode;
    private String country;
}
