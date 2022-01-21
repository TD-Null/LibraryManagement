package com.example.LibraryManagement.tests;

import com.example.LibraryManagement.components.repositories.accounts.AccountNotificationRepository;
import com.example.LibraryManagement.components.repositories.accounts.LibrarianRepository;
import com.example.LibraryManagement.components.repositories.accounts.LibraryCardRepository;
import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.repositories.books.*;
import com.example.LibraryManagement.components.repositories.fines.*;
import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.components.services.accounts.LibrarianServiceImp;
import com.example.LibraryManagement.components.services.accounts.MemberServiceImp;
import com.example.LibraryManagement.components.services.catalogs.UpdateCatalogServiceImp;
import com.example.LibraryManagement.components.services.catalogs.ViewCatalogServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class ServiceImpTests
{
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
        daniel = danielCard.getMember();

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
        sarah = sarahCard.getMember();

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
        kyle = kyleCard.getMember();

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
        manny = mannyCard.getLibrarian();
    }

    // Remove members and librarians after testing.
    @AfterEach
    void tearDown()
    {
        accountService.deleteMember(daniel, danielCard);
        accountService.deleteMember(sarah, sarahCard);
        accountService.deleteMember(kyle, kyleCard);
        accountService.deleteLibrarian(manny, mannyCard);
    }

    // Check if accounts have been registered.
    @Test
    @Order(1)
    void registerAccounts()
    {
        Assertions.assertEquals(danielCard,
                accountService.authenticateUser(danielCard.getCardNumber(), daniel.getPassword()));
        Assertions.assertEquals(sarahCard,
                accountService.authenticateUser(sarahCard.getCardNumber(), sarah.getPassword()));
        Assertions.assertEquals(kyleCard,
                accountService.authenticateUser(kyleCard.getCardNumber(), kyle.getPassword()));
        Assertions.assertEquals(mannyCard,
                accountService.authenticateUser(mannyCard.getCardNumber(), manny.getPassword()));
    }

    @Test
    @Order(2)
    void updateAccounts() {
    }
}