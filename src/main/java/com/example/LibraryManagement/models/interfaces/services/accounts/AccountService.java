package com.example.LibraryManagement.models.interfaces.services.accounts;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

// Methods used in a service component relating to accounts.
public interface AccountService
{
    ResponseEntity<Object> getAccountDetails(Long barcode, String number);

    ResponseEntity<MessageResponse> updateAccountDetails(Long barcode, String number, String name,
                                                         String streetAddress, String city, String zipcode,
                                                         String country, String email, String phoneNumber);

    ResponseEntity<MessageResponse> changePassword(Long barcode, String originalPassword, String newPassword);

    ResponseEntity<LibraryCard> authenticateUser(String libraryCardNumber, String password);

    ResponseEntity<LibraryCard> registerMember(String name, String password, String email,
                                               String streetAddress, String city, String zipcode,
                                               String country, String phoneNumber);

    ResponseEntity<LibraryCard> registerLibrarian(String name, String password, String email,
                                                  String streetAddress, String city, String zipcode,
                                                  String country, String phoneNumber);

    ResponseEntity<MessageResponse> updateMemberStatus(Long memberID, AccountStatus status);

    ResponseEntity<MessageResponse> cancelMemberAccount(Long barcode, String cardNumber, String password);

    ResponseEntity<MessageResponse> cancelLibrarianAccount(Long barcode, String cardNumber);

    Object barcodeReader(Long barcode, String number, AccountType type, AccountStatus status);
}
