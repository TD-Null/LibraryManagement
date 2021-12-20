package com.example.LibraryManagement.models.datatypes;

import lombok.Data;

/*
 * Used to contain each user's/account's details within
 * the system. This includes:
 *
 * Name
 * Address
 * Email
 * Phone Number
 */
@Data
public class Person
{
    private String name;
    private Address address;
    private String email;
    private String phone;
}
