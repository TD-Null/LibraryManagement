package com.example.LibraryManagement.models.datatypes;

import lombok.*;

import javax.validation.constraints.NotBlank;

/*
 * Contains details regarding a user's/account's address,
 * including:
 *
 * Street Address
 * City
 * Zipcode
 * Country
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Address
{
    @NotBlank
    private String streetAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String zipcode;

    @NotBlank
    private String country;
}
