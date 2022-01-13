package com.example.LibraryManagement.models.io.responses;


public class ValidationMessages
{
    // Messages for account authentication and editing.
    public static final String cardNumberMsg = "Please insert the number on your library card.";
    public static final String nameMsg = "Please insert your name.";
    public static final String passwordMsg = "Please insert your password.";
    public static final String newPasswordMsg = "Please insert your new password.";
    public static final String passwordSizeMsg = "Please insert at least 6 characters " +
            "and at most 40 characters for your password.";
    public static final String emailMsg = "Please insert your email.";
    public static final String emailSizeMsg = "Please insert an email at most 50 characters.";
    public static final String emailValidMsg = "Please insert a valid email address.";
    public static final String phoneMsg = "Please insert your phone number.";

    // Messages for addresses.
    public static final String streetMsg = "Please insert the name of your street.";
    public static final String cityMsg = "Please insert the name of your city.";
    public static final String zipcodeMsg = "Please insert your zipcode number.";
    public static final String countryMsg = "Please insert the name of your country.";

    // Messages for card validation.
    public static final String barcodeValidationMsg = "Library card barcode is missing.";
    public static final String numberValidationMsg = "Library card number is missing.";

    // Messages for libraries and books.
    public static final String libraryMsg = "Please insert the name of the library.";

    // Messages for fines and transactions.

}
