package com.example.LibraryManagement.models.interfaces.services;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import org.springframework.http.ResponseEntity;

// Methods used in a service component relating to accounts.
public interface AccountService
{
    public ResponseEntity<LibraryCard> authenticateUser(String libraryCardNumber, String password);

    public ResponseEntity<LibraryCard> registerMember(String name, String password, String email,
                                                    String streetAddress, String city, String zipcode,
                                                    String country, String phoneNumber);
}
