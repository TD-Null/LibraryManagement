package com.example.LibraryManagement.models.datatypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Person
{
    private String name;
    private Address address;
    private String email;
    private String phone;
}
