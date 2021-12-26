package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.datatypes.Person;
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

@AllArgsConstructor
@Service
public class AccountServiceImp implements AccountService
{
    @Autowired
    private final LibraryCardRepository libraryCardRepository;

    @Autowired
    private final MemberRepository memberRepository;

    public ResponseEntity<LibraryCard> authenticateUser(String libraryCardNumber, String password)
    {
        Optional<LibraryCard> cardValidate = libraryCardRepository.findLibraryCardByCardNumber(libraryCardNumber);

        if(cardValidate.isPresent())
        {
            LibraryCard card = cardValidate.get();
            AccountType type = card.getType();

            if(card.isActive() && (
                    (type == AccountType.MEMBER && card.getMember().getPassword().equals(password)) ||
                    (type == AccountType.LIBRARIAN && card.getLibrarian().getPassword().equals(password))))
            {
                return ResponseEntity.ok(card);
            }
        }

        throw new ApiRequestException("Wrong library card number or password.");
    }


    // TODO: Add functionality to check if the given user details are valid, such as making sure the email is unique.
    @Transactional
    public ResponseEntity<LibraryCard> registerMember(String name, String password, String email,
                                                    String streetAddress, String city, String zipcode,
                                                    String country, String phoneNumber)
    {
        // First, get the current date when creating this account.
        Date currDate = new Date((System.currentTimeMillis()));

        // Use the given details of the user to create an account and save to the repository.
        Address address = new Address(streetAddress, city, zipcode, country);
        Person details = new Person(name, address, email, phoneNumber);
        Member member = new Member(password, AccountStatus.ACTIVE, details, currDate);
        memberRepository.save(member);

        // Create a new library card for the user.
        String cardNumber = generateCardNumber();
        LibraryCard libraryCard = new LibraryCard(AccountType.MEMBER, cardNumber, currDate, true);
        libraryCardRepository.save(libraryCard);

        // Link the library card to the user's account.
        member.setLibraryCard(libraryCard);

        return ResponseEntity.ok(libraryCard);
    }

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
