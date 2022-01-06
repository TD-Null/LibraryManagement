package com.example.LibraryManagement.models.interfaces.services.accounts;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

// Methods used in a service component relating to accounts.
public interface AccountService
{
    ResponseEntity<Object> getAccountDetails(Long barcode);

    ResponseEntity<LibraryCard> authenticateUser(String libraryCardNumber, String password);

    ResponseEntity<LibraryCard> registerMember(String name, String password, String email,
                                               String streetAddress, String city, String zipcode,
                                               String country, String phoneNumber);

    ResponseEntity<MessageResponse> updateMemberStatus(Long memberID, AccountStatus status);

    Object barcodeReader(Long barcode, AccountType type, AccountStatus status);
}
