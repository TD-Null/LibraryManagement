package com.example.LibraryManagement.models.interfaces.services.accounts;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import org.springframework.http.ResponseEntity;

// Methods used in a service component relating to accounts.
public interface AccountService
{
    ResponseEntity<LibraryCard> authenticateUser(String libraryCardNumber, String password);

    ResponseEntity<LibraryCard> registerMember(String name, String password, String email,
                                                    String streetAddress, String city, String zipcode,
                                                    String country, String phoneNumber);

    boolean barcodeReader(String barcode, AccountType type);
}
