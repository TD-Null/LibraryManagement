package com.example.LibraryManagement.tests;

import com.example.LibraryManagement.components.repositories.accounts.LibrarianRepository;
import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.enums.accounts.AccountType;
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
                "1024AM",
                "manny@mail.com",
                "manny's street",
                "manny's city",
                "444444",
                "US",
                "9543138282",
                new Date()).getBody();
    }

    // Check if accounts have been registered.
    @Test
    @Order(1)
    void registerAccounts()
    {
        member = (Member) accountService.getAccountDetails(memberCard, memberCard.getCardNumber()).getBody();
        librarian = (Librarian) accountService.getAccountDetails(librarianCard, librarianCard.getCardNumber()).getBody();

        Assertions.assertEquals(AccountType.MEMBER, memberCard.getType());
        Assertions.assertTrue(memberCard.isActive());
        Assertions.assertEquals(member, memberCard.getMember());

        Assertions.assertEquals(AccountType.LIBRARIAN, librarianCard.getType());
        Assertions.assertTrue(librarianCard.isActive());
        Assertions.assertEquals(librarian, librarianCard.getLibrarian());
    }

    @Test
    @Order(2)
    void updateAccounts()
    {
    }

    @Test
    @Order(3)
    void cancelAccounts()
    {
    }
}