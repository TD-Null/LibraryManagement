package com.example.LibraryManagement.tests;

import com.example.LibraryManagement.components.repositories.accounts.AccountNotificationRepository;
import com.example.LibraryManagement.components.repositories.books.BookLendingRepository;
import com.example.LibraryManagement.components.repositories.books.BookReservationRepository;
import com.example.LibraryManagement.components.repositories.fines.CashTransactionRepository;
import com.example.LibraryManagement.components.repositories.fines.CheckTransactionRepository;
import com.example.LibraryManagement.components.repositories.fines.CreditCardTransactionRepository;
import com.example.LibraryManagement.components.repositories.fines.FineTransactionRepository;
import com.example.LibraryManagement.components.services.accounts.MemberServiceImp;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.libraries.Rack;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

/*
 * Test cases used to test the logic and processes of
 * the member service.
 */
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class MemberServiceImpTests
{
    // Repositories used for testing.
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

    // Services being tested.
    @InjectMocks
    private MemberServiceImp memberService;

    // Sample book items.
    private static BookItem book1;
    private static BookItem book2;
    private static BookItem book3;
    private static BookItem book4;

    // Sample members.
    private static Member member1;
    private static Member member2;

    @BeforeAll
    static void setUpAll()
    {
        book1 = new BookItem(
                "921-1-90-113401-2",
                "Action Romance Book",
                "Publisher company",
                "English",
                100,
                new Rack(1, "E"),
                BookFormat.HARDCOVER,
                BookStatus.AVAILABLE,
                new Date(),
                false,
                10.0
        );

        book1.addSubject(new Subject("Action"));
        book1.addSubject(new Subject("Romance"));

        book2 = new BookItem(
                "812-5-57-150290-3",
                "Horror Suspense Book",
                "Publisher company",
                "English",
                100,
                new Rack(1, "E"),
                BookFormat.HARDCOVER,
                BookStatus.AVAILABLE,
                new Date(),
                false,
                5.0
        );

        book2.addSubject(new Subject("Horror"));
        book2.addSubject(new Subject("Suspense"));

        book3 = new BookItem(
                "130-10-0-823359-1",
                "Comedy Book",
                "Publisher company",
                "English",
                100,
                new Rack(1, "E"),
                BookFormat.HARDCOVER,
                BookStatus.AVAILABLE,
                new Date(),
                false,
                5.0
        );

        book3.addSubject(new Subject("Comedy"));

        book4 = new BookItem(
                "902-66-2-033278-7",
                "Drama Book",
                "Publisher company",
                "English",
                100,
                new Rack(1, "E"),
                BookFormat.HARDCOVER,
                BookStatus.AVAILABLE,
                new Date(),
                false,
                5.0
        );

        book4.addSubject(new Subject("Horror"));

        member1 = new Member("Daniel Manning",
                "0824DM",
                AccountStatus.ACTIVE,
                new Address(
                "user1@mail.com",
                "user1's street",
                "user1's city",
                "111111"),
                "US",
                "9541087310",
                new Date());

        member2 = new Member("David Stuart",
                "1309DS",
                AccountStatus.ACTIVE,
                new Address(
                        "user2@mail.com",
                        "user2's street",
                        "user2's city",
                        "222222"),
                "US",
                "9543510389",
                new Date());
    }

    @AfterAll
    static void tearDownAll()
    {
        book1 = null;
        book2 = null;
        book3 = null;
        book4 = null;

        member1 = null;
        member2 = null;
    }

    @Test
    @Order(1)
    void borrowBook()
    {

    }

    @Test
    @Order(2)
    void reserveBook()
    {

    }

    @Test
    @Order(3)
    void renewBook()
    {

    }

    @Test
    @Order(4)
    void returnBook()
    {

    }
}
