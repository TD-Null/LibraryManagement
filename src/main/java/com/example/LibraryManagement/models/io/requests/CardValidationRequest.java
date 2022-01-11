package com.example.LibraryManagement.models.io.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * Class containing a single required input for executing some
 * API requests when only the barcode of a user's library card
 * is required.
 */
@Getter
@AllArgsConstructor
public class CardValidationRequest
{
    @NotNull
    private final Long barcode;

    @NotBlank
    private final String number;
}
