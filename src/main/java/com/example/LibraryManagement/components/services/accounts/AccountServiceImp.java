package com.example.LibraryManagement.components.services.accounts;

import com.example.LibraryManagement.components.repositories.accounts.LibrarianRepository;
import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.interfaces.services.accounts.AccountService;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
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
    private final LibrarianRepository librarianRepository;
    @Autowired
    private final MemberRepository memberRepository;

    // Returns the user's account details using their library card's barcode.
    public ResponseEntity<Object> getAccountDetails(LibraryCard card, String number)
    {
        AccountType type = card.getType();

        if(!card.getCardNumber().equals(number))
            throw new ApiRequestException("Invalid credentials.", HttpStatus.UNAUTHORIZED);

        else if(type == AccountType.MEMBER && card.getMember() != null)
        {
            return ResponseEntity.ok(card.getMember());
        }

        else if(type == AccountType.LIBRARIAN && card.getLibrarian() != null)
        {
            return ResponseEntity.ok(card.getLibrarian());
        }

        /*
         * Else, the user is not found within the database as either
         * a MEMBER or LIBRARIAN and unable to return any account details.
         */
        throw new ApiRequestException("Unable to find user's details within the system.",
                HttpStatus.NOT_FOUND);
    }

    // Authenticates the users credentials with their given library card number and password to login into their account.
    public ResponseEntity<LibraryCard> authenticateUser(String libraryCardNumber, String password)
    {
        Optional<LibraryCard> cardValidation = libraryCardRepository
                .findLibraryCardByCardNumber(libraryCardNumber);

        // Ensure that the card exists within the database of the system using its card number.
        if(cardValidation.isPresent())
        {
            LibraryCard card = cardValidation.get();
            AccountType type = card.getType();

            /*
             * Check if the card is active. If so, check that the user's account is
             * still active, either of a MEMBER or LIBRARIAN, and if the user's password
             * matches to their account corresponding to the library card's details given.
             * If the password matches to their account, then the login is successful and
             * the details of the library card are given.
             */
            if(card.isActive() && (
                    (type == AccountType.MEMBER && card.getMember() != null
                            && card.getMember().getStatus() == AccountStatus.ACTIVE
                            && card.getMember().getPassword().equals(password)) ||
                    (type == AccountType.LIBRARIAN && card.getLibrarian() != null
                            && card.getLibrarian().getStatus() == AccountStatus.ACTIVE
                            && card.getLibrarian().getPassword().equals(password))))
            {
                return ResponseEntity.ok(card);
            }
        }

        // Else, throw an API request exception stating that the given credentials were invalid.
        throw new ApiRequestException("Invalid credentials. (Wrong library card number or password)",
                HttpStatus.UNAUTHORIZED);
    }

    // Registers a new member using the user's inputted details to create an account.
    @Transactional
    public ResponseEntity<LibraryCard> registerMember(String name, String password, String email,
                                                      String streetAddress, String city, String zipcode,
                                                      String country, String phoneNumber, Date currDate)
    {
        // First, validate that the user's email isn't already being used in the website's other user accounts.
        if(memberRepository.findMemberByEmail(email).isPresent())
        {
            throw new ApiRequestException("Failed to create the account with the given credentials.",
                    HttpStatus.CONFLICT);
        }

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
        libraryCard.setMember(member);

        // Return the details of the user's library card after the account has been successfully created.
        return ResponseEntity.ok(libraryCard);
    }

    // Registers a new librarian using the user's inputted details to create an account.
    @Transactional
    public ResponseEntity<LibraryCard> registerLibrarian(String name, String password, String email,
                                                         String streetAddress, String city, String zipcode,
                                                         String country, String phoneNumber, Date currDate)
    {
        // First, validate that the user's email isn't already being used in the website's other user accounts.
        if(librarianRepository.findLibrarianByEmail(email).isPresent())
        {
            throw new ApiRequestException("Failed to create the account with the given credentials.",
                    HttpStatus.CONFLICT);
        }

        // Use the given details of the user to create an account and save to the repository.
        Address address = new Address(streetAddress, city, zipcode, country);
        Librarian librarian = new Librarian(name, password, AccountStatus.ACTIVE,
                address, email, phoneNumber);
        librarianRepository.save(librarian);

        // Create a new library card for the user.
        String cardNumber = generateCardNumber();
        LibraryCard libraryCard = new LibraryCard(AccountType.LIBRARIAN, cardNumber, currDate, true);
        libraryCardRepository.save(libraryCard);

        // Link the library card to the user's account.
        librarian.setLibraryCard(libraryCard);
        libraryCard.setLibrarian(librarian);

        // Return the details of the user's library card after the account has been successfully created.
        return ResponseEntity.ok(libraryCard);
    }

    @Transactional
    public ResponseEntity<MessageResponse> updateAccountDetails(LibraryCard card, String number, String name,
                                                                String streetAddress, String city, String zipcode,
                                                                String country, String email, String phoneNumber)
    {
        AccountType type = card.getType();

        if(!card.getCardNumber().equals(number))
            throw new ApiRequestException("Invalid credentials.", HttpStatus.UNAUTHORIZED);

        else if(type == AccountType.MEMBER && card.getMember() != null)
        {
            Member member = card.getMember();
            member.setName(name);
            member.setAddress(new Address(streetAddress, city, zipcode, country));
            member.setEmail(email);
            member.setPhoneNumber(phoneNumber);

            return ResponseEntity.ok(new MessageResponse("Successfully updated user's details within the system."));
        }

        else if(type == AccountType.LIBRARIAN && card.getLibrarian() != null)
        {
            Librarian librarian = card.getLibrarian();
            librarian.setName(name);
            librarian.setAddress(new Address(streetAddress, city, zipcode, country));
            librarian.setEmail(email);
            librarian.setPhoneNumber(phoneNumber);

            return ResponseEntity.ok(new MessageResponse("Successfully updated user's details within the system."));
        }

        /*
         * Else, the user is not found within the database as either
         * a MEMBER or LIBRARIAN and unable to return any account details.
         */
        throw new ApiRequestException("Unable to find user's details within the system.",
                HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<MessageResponse> changePassword(LibraryCard card,
                                                          String originalPassword, String newPassword)
    {
        AccountType type = card.getType();

        if(type == AccountType.MEMBER && card.getMember() != null)
        {
            Member member = card.getMember();

            if(!member.getPassword().equals(originalPassword))
            {
                throw new ApiRequestException("Invalid Password.", HttpStatus.UNAUTHORIZED);
            }

            member.setPassword(newPassword);

            return ResponseEntity.ok(new MessageResponse("Successfully changed user's password within the system."));
        }

        else if(type == AccountType.LIBRARIAN && card.getLibrarian() != null)
        {
            Librarian librarian = card.getLibrarian();

            if(!librarian.getPassword().equals(originalPassword))
            {
                throw new ApiRequestException("Invalid Password.", HttpStatus.UNAUTHORIZED);
            }

            librarian.setPassword(newPassword);

            return ResponseEntity.ok(new MessageResponse("Successfully changed user's password within the system."));
        }

        /*
         * Else, the user is not found within the database as either
         * a MEMBER or LIBRARIAN and unable to return any account details.
         */
        throw new ApiRequestException("Unable to find user's details within the system.",
                HttpStatus.NOT_FOUND);
    }

    // Updates a member's account status using the member's ID and the given status update.
    @Transactional
    public ResponseEntity<MessageResponse> updateMemberStatus(Long memberID, AccountStatus status)
    {
        Optional<Member> memberValidation = memberRepository.findById(memberID);

        // First, validate that the given user is a member within the system.
        if(memberValidation.isPresent())
        {
            Member member = memberValidation.get();
            AccountStatus currStatus = member.getStatus();

            // If the member's account is already CANCELLED, its status cannot be updated.
            if(currStatus == AccountStatus.CANCELLED)
                throw new ApiRequestException("The member's account is inactive. The account's status cannot be updated.",
                        HttpStatus.FORBIDDEN);

            // Else, update the member's account status and return a response.
            else
            {
                member.setStatus(status);
                return ResponseEntity.ok(new MessageResponse("Member's account status has been updated successfully."));
            }
        }

        // Else, the account's status cannot be updated as it cannot be found.
        throw new ApiRequestException("Unable to find member's account within the system.",
                HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<MessageResponse> cancelMemberAccount(LibraryCard card,
                                                               String cardNumber, String password)
    {
        if(card.getType() == AccountType.MEMBER)
        {
            Member member = card.getMember();

            if(member == null)
                throw new ApiRequestException("No member is associated with this card.",
                        HttpStatus.BAD_REQUEST);

            else if(member.getStatus() == AccountStatus.CANCELLED)
                throw new ApiRequestException("Member has already cancelled this account",
                        HttpStatus.BAD_REQUEST);

            else if(member.getStatus() == AccountStatus.BLACKLISTED)
                throw new ApiRequestException("Member's account is currently blocked. " +
                        "User cannot cancel their membership currently.",
                        HttpStatus.FORBIDDEN);

            else if(!card.isActive())
                throw new ApiRequestException("Card is currently inactive, " +
                        "so membership cannot be cancelled.",
                        HttpStatus.FORBIDDEN);

            else if(!card.getCardNumber().equals(cardNumber) || !member.getPassword().equals(password))
                throw new ApiRequestException("Given credentials are invalid." +
                        "Cannot proceed with cancelling member's account",
                        HttpStatus.UNAUTHORIZED);

            else if(member.getIssuedBooksTotal() > 0)
                throw new ApiRequestException("User currently has books still issued to their account." +
                        "Please return any loaned books and cancel any book reservations made.",
                        HttpStatus.FORBIDDEN);

            else if(member.getTotalFines() > 0)
                throw new ApiRequestException("User currently has fines still associated with their account." +
                        "Please pay for any fines still present in your account.",
                        HttpStatus.FORBIDDEN);

            card.setActive(false);
            member.setStatus(AccountStatus.CANCELLED);
            return ResponseEntity.ok(new MessageResponse("User has successfully cancelled their membership."));
        }

        throw new ApiRequestException("User is not a member within the system.",
                HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<MessageResponse> cancelLibrarianAccount(LibraryCard card, String cardNumber)
    {
        if(card.getType() == AccountType.LIBRARIAN)
        {
            Librarian librarian = card.getLibrarian();

            if(librarian == null)
                throw new ApiRequestException("No librarian is associated with this card.",
                        HttpStatus.BAD_REQUEST);

            else if(librarian.getStatus() == AccountStatus.CANCELLED)
                throw new ApiRequestException("Librarian's account has already been cancelled.",
                        HttpStatus.BAD_REQUEST);

            else if(librarian.getStatus() == AccountStatus.BLACKLISTED)
                throw new ApiRequestException("Librarian's account is currently blocked. " +
                        "User cannot cancel their account currently.",
                        HttpStatus.FORBIDDEN);

            else if(!card.isActive())
                throw new ApiRequestException("Card is currently inactive, " +
                        "so membership cannot be cancelled.",
                        HttpStatus.FORBIDDEN);

            else if(!card.getCardNumber().equals(cardNumber))
                throw new ApiRequestException("Given credentials are invalid." +
                        "Cannot proceed with cancelling librarian's account",
                        HttpStatus.UNAUTHORIZED);

            card.setActive(false);
            librarian.setStatus(AccountStatus.CANCELLED);
            return ResponseEntity.ok(new MessageResponse(
                    "Librarian's account has been successfully cancelled within the system."));
        }

        throw new ApiRequestException("User is not a librarian within the system.",
                HttpStatus.NOT_FOUND);
    }

    /*
     * A barcode reader method that validates the library card of the user and their account.
     * This is also used to verify that the right type of account is being validated before
     * taking any action.
     *
     * For example, only librarians can be able to add and modify books.
     */
    public Object barcodeReader(Long barcode, String number, AccountType type, AccountStatus status)
    {
        Optional<LibraryCard> cardValidation = libraryCardRepository.findById(barcode);

        // Ensure that the card exists within the database of the system using its barcode.
        if(cardValidation.isPresent())
        {
            LibraryCard card = cardValidation.get();

            if(!card.getCardNumber().equals(number))
                throw new ApiRequestException("Invalid credentials.", HttpStatus.UNAUTHORIZED);

            /*
             * Check if the card is active and the account type matches to what is
             * expected. If so, check that the user's account is still active, either
             * of a MEMBER or LIBRARIAN. If both the library card and user's account is
             * still active, then the user may proceed.
             */
            else if (card.isActive() && card.getType() == type)
            {
                if(type == AccountType.MEMBER && card.getMember() != null
                        && card.getMember().getStatus() == status)
                {
                    return card.getMember();
                }

                else if(type == AccountType.LIBRARIAN && card.getLibrarian() != null
                            && card.getLibrarian().getStatus() == status)
                {
                    return card.getLibrarian();
                }
            }
        }

        // Else, the user will be unable to proceed with any action.
        throw new ApiRequestException("User is not allowed to perform this action.",
                HttpStatus.UNAUTHORIZED);
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
