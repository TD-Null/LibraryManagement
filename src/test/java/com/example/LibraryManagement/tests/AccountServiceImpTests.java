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
        accountService = new AccountServiceImp(libraryCardRepository, librarianRepository, memberRepository);

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
    }

    // Set all variables to null after each test.
    @AfterEach
    void tearDown()
    {
        accountService = null;
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
        member = (Member) accountService.getAccountDetails(memberCard, memberCard.getCardNumber()).getBody();
        librarian = (Librarian) accountService.getAccountDetails(librarianCard, librarianCard.getCardNumber()).getBody();

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
        member = (Member) accountService.getAccountDetails(
                memberCard, memberCard.getCardNumber()).getBody();
        librarian = (Librarian) accountService.getAccountDetails(
                librarianCard, librarianCard.getCardNumber()).getBody();
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
    void cancelAccounts()
    {
    }
}