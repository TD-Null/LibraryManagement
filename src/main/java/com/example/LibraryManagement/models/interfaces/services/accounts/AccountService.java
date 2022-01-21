package com.example.LibraryManagement.models.interfaces.services.accounts;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.Date;

// Methods used in a service component relating to accounts.
public interface AccountService
{
    ResponseEntity<Object> getAccountDetails(LibraryCard card, String number);

    ResponseEntity<LibraryCard> authenticateUser(String libraryCardNumber, String password);

    ResponseEntity<LibraryCard> registerMember(String name, String password, String email,
                                               String streetAddress, String city, String zipcode,
                                               String country, String phoneNumber, Date currDate);

    ResponseEntity<LibraryCard> registerLibrarian(String name, String password, String email,
                                                  String streetAddress, String city, String zipcode,
                                                  String country, String phoneNumber, Date currDate);

    ResponseEntity<MessageResponse> updateAccountDetails(LibraryCard card, String number, String name,
                                                         String streetAddress, String city, String zipcode,
                                                         String country, String email, String phoneNumber);

    ResponseEntity<MessageResponse> changePassword(LibraryCard card, String originalPassword, String newPassword);

    ResponseEntity<MessageResponse> updateMemberStatus(Member member, AccountStatus status);

    ResponseEntity<MessageResponse> cancelMemberAccount(LibraryCard card, String cardNumber, String password);

    ResponseEntity<MessageResponse> cancelLibrarianAccount(LibraryCard card, String cardNumber);

    Object barcodeReader(LibraryCard card, String number, AccountType type, AccountStatus status);
}
