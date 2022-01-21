package com.example.LibraryManagement.tests;

import com.example.LibraryManagement.components.repositories.accounts.LibrarianRepository;
import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class AccountServiceImpTests
{
    // Repositories used for testing.
    @Mock
    private LibraryCardRepository libraryCardRepository;
    @Mock
    private LibrarianRepository librarianRepository;
    @Mock
    private MemberRepository memberRepository;

    // Services being tested.
    @InjectMocks
    private AccountServiceImp accountService;

    // Sample members and librarians.
    private Member member;
    private Librarian librarian;

    // Sample library cards;
    private LibraryCard memberCard;
    private LibraryCard librarianCard;

    // Add members and librarians before testing.
    @BeforeEach
    void setUp()
    {
        memberCard = accountService.registerMember(
                "Daniel Manning",
                "0824DM",
                "user@mail.com",
                "user's street",
                "user's city",
                "111111",
                "US",
                "9541087310",
                new Date()).getBody();

        librarianCard = accountService.registerLibrarian(
                "Manny South",
                "1024MS",
                "librarian@mail.com",
                "librarian's street",
                "librarian's city",
                "444444",
                "US",
                "9543138282",
                new Date()).getBody();

        member = (Member) accountService.getAccountDetails(
                memberCard, memberCard.getCardNumber()).getBody();
        librarian = (Librarian) accountService.getAccountDetails(
                librarianCard, librarianCard.getCardNumber()).getBody();
    }

    // Set all variables to null after each test.
    @AfterEach
    void tearDown()
    {
        member = null;
        librarian = null;
        memberCard = null;
        librarianCard = null;
    }

    // Test if accounts have been registered.
    @Test
    @Order(1)
    void registerAccounts()
    {
        // Check if the service has returned the right account information
        // for both the member and librarian.
        Assertions.assertEquals(AccountType.MEMBER, memberCard.getType());
        Assertions.assertTrue(memberCard.isActive());
        Assertions.assertEquals(member, memberCard.getMember());
        Assertions.assertNull(memberCard.getLibrarian());
        Assertions.assertEquals(member,
                accountService.barcodeReader(memberCard, memberCard.getCardNumber(),
                        AccountType.MEMBER, AccountStatus.ACTIVE));

        Assertions.assertEquals(AccountType.LIBRARIAN, librarianCard.getType());
        Assertions.assertTrue(librarianCard.isActive());
        Assertions.assertEquals(librarian, librarianCard.getLibrarian());
        Assertions.assertNull(librarianCard.getMember());
        Assertions.assertEquals(librarian,
                accountService.barcodeReader(librarianCard, librarianCard.getCardNumber(),
                        AccountType.LIBRARIAN, AccountStatus.ACTIVE));
    }

    // Test if accounts are updated properly.
    @Test
    @Order(2)
    void updateAccounts()
    {
        String memberExceptionMessage = "";
        String librarianExceptionMessage = "";

        accountService.updateAccountDetails(
                memberCard,
                memberCard.getCardNumber(),
                "Daniel Manning",
                "daniel's street",
                "daniel's city",
                "111111",
                "US",
                "daniel@mail.com",
                "9541087310").getBody();

        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.updateAccountDetails(
                memberCard,
                "123456",
                "Daniel Manning",
                "daniel's street",
                "daniel's city",
                "111111",
                "US",
                "daniel@mail.com",
                "9541087310");
        }).getMessage();
        Assertions.assertEquals("Invalid credentials.", memberExceptionMessage);

        accountService.changePassword(memberCard, member.getPassword(), "9035DM");
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.changePassword(memberCard, "1234DM", "1029DM");
        }).getMessage();
        Assertions.assertEquals("Invalid password.", memberExceptionMessage);

        accountService.updateAccountDetails(
                librarianCard,
                librarianCard.getCardNumber(),
                "Manny South",
                "manny's street",
                "manny's city",
                "444444",
                "US",
                "manny@mail.com",
                "9543138282").getBody();

        librarianExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.updateAccountDetails(
                    librarianCard,
                    "123456",
                    "Manny South",
                    "manny's street",
                    "manny's city",
                    "444444",
                    "US",
                    "manny@mail.com",
                    "9543138282");
        }).getMessage();
        Assertions.assertEquals("Invalid credentials.", librarianExceptionMessage);

        accountService.changePassword(librarianCard, librarian.getPassword(), "2039MS");
        librarianExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.changePassword(librarianCard, "1234MS", "1029MS");
        }).getMessage();
        Assertions.assertEquals("Invalid password.", librarianExceptionMessage);

        Assertions.assertEquals("daniel@mail.com", member.getEmail());
        Assertions.assertEquals(new Address(
                "daniel's street",
                        "daniel's city",
                        "111111",
                        "US"),
                member.getAddress());
        Assertions.assertEquals("9035DM", member.getPassword());

        Assertions.assertEquals("manny@mail.com", librarian.getEmail());
        Assertions.assertEquals(new Address(
                        "manny's street",
                        "manny's city",
                        "444444",
                        "US"),
                librarian.getAddress());
        Assertions.assertEquals("2039MS", librarian.getPassword());
    }

    @Test
    @Order(3)
    void cancelMemberAccounts()
    {
        String memberExceptionMessage = "";
        String librarianExceptionMessage = "";

        // Check that the account is of a librarian.
        Assertions.assertEquals(librarian,
                accountService.barcodeReader(librarianCard, librarianCard.getCardNumber(),
                        AccountType.LIBRARIAN, AccountStatus.ACTIVE));

        // Have the librarian block the member's account without throwing an exception.
        Assertions.assertDoesNotThrow(() -> {
            accountService.updateMemberStatus(member, AccountStatus.BLACKLISTED);
        });
        Assertions.assertEquals(AccountStatus.BLACKLISTED, member.getStatus());

        // If the librarian tries to block the member again, an exception should be thrown.
        librarianExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.updateMemberStatus(member, AccountStatus.BLACKLISTED);
        }).getMessage();
        Assertions.assertEquals("This member's account is already blacklisted.",
                librarianExceptionMessage);

        // If the member tries to cancel their account while it is blocked, an exception should be thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), member.getPassword());
        }).getMessage();
        Assertions.assertEquals("Member's account is currently blocked. " +
                "User cannot cancel their membership currently.", memberExceptionMessage);

        // Have the librarian unblock the member's account without throwing an exception.
        Assertions.assertDoesNotThrow(() -> {
            accountService.updateMemberStatus(member, AccountStatus.ACTIVE);
        });
        Assertions.assertEquals(AccountStatus.ACTIVE, member.getStatus());

        // If the librarian tries to unblock the member again, an exception should be thrown.
        librarianExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.updateMemberStatus(member, AccountStatus.ACTIVE);
        }).getMessage();
        Assertions.assertEquals("This member's account is already active.",
                librarianExceptionMessage);

        // If the library card is not of a member type and they try to cancel their account,
        // an exception is thrown
        memberCard.setType(AccountType.LIBRARIAN);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), member.getPassword());
        }).getMessage();
        Assertions.assertEquals("User is not a member within the system.",
                memberExceptionMessage);
        memberCard.setType(AccountType.MEMBER);

        // If the member's library card has no member association and they try to cancel their account,
        // an exception is thrown.
        memberCard.setMember(null);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), member.getPassword());
        }).getMessage();
        Assertions.assertEquals("No member is associated with this card.",
                memberExceptionMessage);
        memberCard.setMember(member);

        // If the member's library card is inactive and they try to cancel their account,
        // an exception is thrown.
        memberCard.setActive(false);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), member.getPassword());
        }).getMessage();
        Assertions.assertEquals("Card is currently inactive, " +
                "so membership cannot be cancelled.", memberExceptionMessage);
        memberCard.setActive(true);

        // If the member gives the wrong credentials to cancel their account,
        // an exception should be thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    "123456", member.getPassword());
        }).getMessage();
        Assertions.assertEquals("Given credentials are invalid." +
                "Cannot proceed with cancelling member's account", memberExceptionMessage);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), "password");
        }).getMessage();
        Assertions.assertEquals("Given credentials are invalid." +
                "Cannot proceed with cancelling member's account", memberExceptionMessage);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    "123456", "password");
        }).getMessage();
        Assertions.assertEquals("Given credentials are invalid." +
                "Cannot proceed with cancelling member's account", memberExceptionMessage);

        // If the member has books still issued to them and tries to cancel their account,
        // an exception is thrown.
        member.setIssuedBooksTotal(5);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), member.getPassword());
        }).getMessage();
        Assertions.assertEquals("User currently has books still issued to their account." +
                "Please return any loaned books and cancel any book reservations made.", memberExceptionMessage);
        member.setIssuedBooksTotal(0);

        // If the member has fines they still have to pay and tries to cancel their account,
        // an exception is thrown.
        member.setTotalFines(5);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), member.getPassword());
        }).getMessage();
        Assertions.assertEquals("User currently has fines still associated with their account." +
                "Please pay for any fines still present in your account.", memberExceptionMessage);
        member.setTotalFines(0);

        // If a member has no issues with their account, then they can cancel their account.
        Assertions.assertDoesNotThrow(() -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), member.getPassword());
        });
        Assertions.assertEquals(AccountStatus.CANCELLED, member.getStatus());

        // If a member tries to cancel their account again,
        // an exception is thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            accountService.cancelMemberAccount(memberCard,
                    memberCard.getCardNumber(), member.getPassword());
        }).getMessage();
        Assertions.assertEquals("Member has already cancelled this account.",
                memberExceptionMessage);
    }

    @Test
    @Order(4)
    void cancelLibrarianAccounts()
    {
        String librarianExceptionMessage = "";

        LibraryCard newLibrarianCard = accountService.registerLibrarian(
                "Librarian",
                "password",
                "lib@mail.com",
                "street",
                "city",
                "123456",
                "US",
                "1231231234",
                new Date()).getBody();
        Librarian newLibrarian = (Librarian) accountService.getAccountDetails(
                newLibrarianCard, newLibrarianCard.getCardNumber())
                .getBody();

        // Check that the accounts are of librarians.
        Assertions.assertEquals(librarian,
                accountService.barcodeReader(librarianCard, librarianCard.getCardNumber(),
                        AccountType.LIBRARIAN, AccountStatus.ACTIVE));
        Assertions.assertEquals(newLibrarian,
                accountService.barcodeReader(newLibrarianCard, newLibrarianCard.getCardNumber(),
                        AccountType.LIBRARIAN, AccountStatus.ACTIVE));

    }
}