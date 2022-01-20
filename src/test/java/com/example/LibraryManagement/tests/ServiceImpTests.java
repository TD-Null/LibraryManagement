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
import com.example.LibraryManagement.models.interfaces.services.accounts.MemberService;
import com.example.LibraryManagement.models.interfaces.services.catalogs.UpdateCatalogService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ServiceImpTests
{
    // Mock repositories for other services.
    @Mock
    private LibrarianRepository librarianRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookLendingRepository bookLendingRepository;
    @Mock
    private BookReservationRepository bookReservationRepository;
    @Mock
    private AccountNotificationRepository notificationRepository;
    @Mock
    private FineTransactionRepository fineTransactionRepository;
    @Mock
    private CreditCardTransactionRepository creditCardTransactionRepository;
    @Mock
    private CheckTransactionRepository checkTransactionRepository;
    @Mock
    private CashTransactionRepository cashTransactionRepository;

    // Mock repositories for ValidationService.
    @Mock
    private LibraryCardRepository libraryCardRepository;
    @Mock
    private FineRepository fineRepository;
    @Mock
    private BookItemRepository bookItemRepository;
    @Mock
    private LibraryRepository libraryRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private AuthorRepository authorRepository;

    // AutoCloseable used to open and close mock repositories before and after each test.
    private AutoCloseable autoCloseable;

    // Services used for testing.
    private AccountServiceImp accountService;
    private LibrarianServiceImp librarianService;
    private MemberServiceImp memberService;
    private ViewCatalogServiceImp viewCatalogService;
    private UpdateCatalogServiceImp updateCatalogService;
    private ValidationService validationService;

    @BeforeAll
    void setup()
    {
        autoCloseable = MockitoAnnotations.openMocks(this);
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
    void tearDown() throws Exception
    {
        autoCloseable.close();
    }

    @Test
    void registerMember()
    {
//        accountService.registerMember("Daniel Manning",
//                "0824DM",
//                "daniel@")
    }

    @Test
    void registerLibrarian() {
    }

    @Test
    void updateAccountDetails() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void updateMemberStatus() {
    }

    @Test
    void cancelMemberAccount() {
    }
}