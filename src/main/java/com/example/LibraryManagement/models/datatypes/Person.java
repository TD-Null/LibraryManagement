package com.example.LibraryManagement.models.datatypes;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * Used to contain each user's/account's details within
 * the system. This includes:
 *
 * Name
 * Address
 * Email
 * Phone Number
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Person
{
    @NotBlank
    private String name;

    @NotNull
    private Address address;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;
}
