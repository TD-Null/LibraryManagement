package com.example.LibraryManagement.models.io.responses;

// Class containing validation messages for API requests.
public class ValidationMessages
{
    // Messages for card validation.
    public static final String barcodeValidationMsg = "Library card barcode is missing.";
    public static final String numberValidationMsg = "Library card number is missing.";

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

    // Messages for fines and transactions.
    public static final String cardTransactionMsg = "Please insert the name on your credit card.";
    public static final String checkBankMsg = "Please insert the name of your bank.";
    public static final String checkNumberMsg = "Please insert the number of your check.";

    // Messages for libraries and books.
    public static final String libraryMsg = "Please insert the name of the library.";
    public static final String bookBarcodeMsg = " Please insert the barcode of the books.";
    public static final String bookISBNMsg = "Please insert the ISBN of the book.";
    public static final String bookTitleMsg = "Please insert the title of the book.";
    public static final String bookPublisherMsg = "Please insert the publisher of the book.";
    public static final String bookLanguageMsg = "Please insert the language of the book.";
    public static final String bookFormatMsg = "Please insert the format of the book.";
    public static final String subjectMsg = "Please insert the name of the subject.";
    public static final String authorMsg = "Please insert the name of the author.";
    public static final String authorDescMsg = "Please insert a description for the author.";

    // Message for Date format.
    public static final String dateFormatMsg = "Date must be in the format of yyyy-MM-dd.";

}
