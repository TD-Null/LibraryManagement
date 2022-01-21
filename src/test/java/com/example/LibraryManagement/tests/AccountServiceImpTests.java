package com.example.LibraryManagement.tests;

import com.example.LibraryManagement.components.repositories.accounts.LibrarianRepository;
import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
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
    private Member daniel;
    private Member sarah;
    private Member kyle;
    private Librarian manny;

    // Sample library cards;
    private LibraryCard danielCard;
    private LibraryCard sarahCard;
    private LibraryCard kyleCard;
    private LibraryCard mannyCard;

    // Add members and librarians before testing.
    @BeforeEach
    void setUp()
    {
        accountService = new AccountServiceImp(libraryCardRepository, librarianRepository, memberRepository);
    }

    // Remove members and librarians after testing.
//    @AfterEach
//    void tearDown()
//    {
//        accountService.deleteMember(daniel, danielCard);
//        accountService.deleteMember(sarah, sarahCard);
//        accountService.deleteMember(kyle, kyleCard);
//        accountService.deleteLibrarian(manny, mannyCard);
//    }

    // Check if accounts have been registered.
    @Test
    @Order(1)
    void registerAccounts()
    {
        danielCard = accountService.registerMember(
                "Daniel Manning",
                "0824DM",
                "daniel@mail.com",
                "daniel's street",
                "daniel's city",
                "111111",
                "US",
                "9541087310",
                new Date()).getBody();

        sarahCard = accountService.registerMember(
                "Sarah Mitchell",
                "9012SM",
                "sarah@mail.com",
                "sarah's street",
                "sarah's city",
                "222222",
                "US",
                "9541504231",
                new Date()).getBody();

        kyleCard = accountService.registerMember(
                "Kyle Ranch",
                "1453KR",
                "kyle@mail.com",
                "kyle's street",
                "kyle's city",
                "333333",
                "US",
                "9542081690",
                new Date()).getBody();

        mannyCard = accountService.registerLibrarian(
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

    @Test
    @Order(2)
    void updateAccounts() {
    }
}