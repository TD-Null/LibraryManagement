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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceImpTests
{
    // Mock repositories for other services.
    @Mock
    private static LibrarianRepository librarianRepository;
    @Mock
    private static MemberRepository memberRepository;
    @Mock
    private static BookLendingRepository bookLendingRepository;
    @Mock
    private static BookReservationRepository bookReservationRepository;
    @Mock
    private static AccountNotificationRepository notificationRepository;
    @Mock
    private static FineTransactionRepository fineTransactionRepository;
    @Mock
    private static CreditCardTransactionRepository creditCardTransactionRepository;
    @Mock
    private static CheckTransactionRepository checkTransactionRepository;
    @Mock
    private static CashTransactionRepository cashTransactionRepository;

    // Mock repositories for ValidationService.
    @Mock
    private static LibraryCardRepository libraryCardRepository;
    @Mock
    private static FineRepository fineRepository;
    @Mock
    private static BookItemRepository bookItemRepository;
    @Mock
    private static LibraryRepository libraryRepository;
    @Mock
    private static SubjectRepository subjectRepository;
    @Mock
    private static AuthorRepository authorRepository;

    // AutoCloseable used to open and close mock repositories before and after each test.
    private static AutoCloseable autoCloseable;

    // Services used for testing.
    private static AccountServiceImp accountService;
    private static LibrarianServiceImp librarianService;
    private static MemberServiceImp memberService;
    private static ViewCatalogServiceImp viewCatalogService;
    private static UpdateCatalogServiceImp updateCatalogService;
    private static ValidationService validationService;

    @BeforeEach
    void setUp()
    {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }


    @BeforeAll
    static void setUpAll()
    {
        validationService = new ValidationService(libraryCardRepository, fineRepository, bookItemRepository,
                libraryRepository, subjectRepository, authorRepository);
        accountService = new AccountServiceImp(libraryCardRepository,
                librarianRepository, memberRepository, validationService);
        librarianService = new LibrarianServiceImp(librarianRepository, memberRepository,
                bookLendingRepository, bookReservationRepository, fineRepository);
        memberService = new MemberServiceImp(bookLendingRepository, bookReservationRepository,
                notificationRepository, fineTransactionRepository, creditCardTransactionRepository,
                checkTransactionRepository, cashTransactionRepository, validationService);
        viewCatalogService = new ViewCatalogServiceImp(bookItemRepository, libraryRepository, authorRepository,
                subjectRepository, validationService);

    }

    @AfterAll
    static void tearDownAll() throws Exception
    {
        autoCloseable.close();
    }

    @Test
    @Order(1)
    void registerAccounts()
    {
        // Start adding members.
        accountService.registerMember("Daniel Manning",
                "0824DM",
                "daniel@mail.com",
                "daniel's street",
                "daniel's city",
                "111111",
                "US",
                "9541087310");

        accountService.registerMember("Sarah Mitchell",
                "9012SM",
                "sarah@mail.com",
                "sarah's street",
                "sarah's city",
                "222222",
                "US",
                "9541504231");

        accountService.registerMember("Kyle Ranch",
                "1453KR",
                "kyle@mail.com",
                "kyle's street",
                "kyle's city",
                "333333",
                "US",
                "9542081690");

        //Start adding librarians.
        accountService.registerLibrarian("Arthur Morgan",
                "1024AM",
                "arthur@mail.com",
                "arthur's street",
                "arthur's city",
                "444444",
                "US",
                "9543138282");

        // Check that users have been added to the system.
        Assertions.assertTrue(memberRepository.findMemberByEmail("daniel@mail.com").isPresent());
        Assertions.assertTrue(memberRepository.findMemberByEmail("sarah@mail.com").isPresent());
        Assertions.assertTrue(memberRepository.findMemberByEmail("kyle@mail.com").isPresent());
        Assertions.assertTrue(librarianRepository.findLibrarianByEmail("arthur@mail.com").isPresent());
    }

    @Test
    @Order(2)
    void updateAccounts() {
    }
}