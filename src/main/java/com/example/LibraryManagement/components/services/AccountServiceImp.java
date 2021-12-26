package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.interfaces.services.AccountService;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Optional;
import java.util.Random;

/*
 * Service component that contains methods relating to accounts:
 *
 * - User authentication for logging into a user's account
 * - Registering users as members with a newly created account using the user's details
 */
@AllArgsConstructor
@Service
public class AccountServiceImp implements AccountService
{
    @Autowired
    private final LibraryCardRepository libraryCardRepository;

    @Autowired
    private final MemberRepository memberRepository;

    // Authenticates the users credentials with their given library card number and password to login into their account.
    public ResponseEntity<LibraryCard> authenticateUser(String libraryCardNumber, String password)
    {
        Optional<LibraryCard> cardValidate = libraryCardRepository
                .findLibraryCardByCardNumber(libraryCardNumber);

        // Ensure that the card exists within the database of the system.
        if(cardValidate.isPresent())
        {
            LibraryCard card = cardValidate.get();
            AccountType type = card.getType();

            /*
             * Check if the card is active. If so, check the user's password,
             * either of a MEMBER or LIBRARIAN, matches their account corresponding
             * to the library card details given. If the password matches to their
             * account, then the login is successful and the details of the library
             * card are given.
             */
            if(card.isActive() && (
                    (type == AccountType.MEMBER && card.getMember().getPassword().equals(password)) ||
                    (type == AccountType.LIBRARIAN && card.getLibrarian().getPassword().equals(password))))
            {
                return ResponseEntity.ok(card);
            }
        }

        // Else, throw an API request exception stating that the given credentials were invalid.
        throw new ApiRequestException("Invalid credentials. (Wrong library card number of password)");
    }

    // Registers a new member using the user's inputted details to create an account.
    @Transactional
    public ResponseEntity<LibraryCard> registerMember(String name, String password, String email,
                                                    String streetAddress, String city, String zipcode,
                                                    String country, String phoneNumber)
    {
        // First, validate that the user's email isn't already being used in the website's other user accounts.
        if(memberRepository.findMemberByEmail(email).isPresent())
        {
            throw new ApiRequestException("Failed to create the account with the given credentials.");
        }

        // Get the current date when creating this account.
        Date currDate = new Date((System.currentTimeMillis()));

        // Use the given details of the user to create an account and save to the repository.
        Address address = new Address(streetAddress, city, zipcode, country);
        Member member = new Member(name, password, AccountStatus.ACTIVE,
                address, email, phoneNumber, currDate);
        memberRepository.save(member);

        // Create a new library card for the user.
        String cardNumber = generateCardNumber();
        LibraryCard libraryCard = new LibraryCard(AccountType.MEMBER, cardNumber, currDate, true);
        libraryCardRepository.save(libraryCard);

        // Link the library card to the user's account.
        member.setLibraryCard(libraryCard);

        // Return the details of the user's library card after the account has been successfully created.
        return ResponseEntity.ok(libraryCard);
    }

    /*
     * Generates a random 6-digit card number to create a new library card.
     * Also ensures that the generated card number is unique across all
     * user accounts.
     */
    private String generateCardNumber()
    {
        String generatedCardNumber;

        do {
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            generatedCardNumber = String.format(("%06d"), number);
        } while(libraryCardRepository.findLibraryCardByCardNumber(generatedCardNumber).isPresent());

        return generatedCardNumber;
    }
}
