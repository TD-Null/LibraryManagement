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
import com.example.LibraryManagement.models.books.properties.Limitations;
import com.example.LibraryManagement.models.books.properties.Subject;
import com.example.LibraryManagement.models.datatypes.Address;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    // Used date pattern and format and dates for checkout and reservations.
    private static String datePattern = "yyyy-MM-dd";
    private static SimpleDateFormat df = new SimpleDateFormat(datePattern);
    private static Date borrowDate1;
    private static Date borrowDate2;
    private static Date borrowDate3;
    private static Date borrowDate4;

    // Exception messages.
    private String memberExceptionMessage;
    private String librarianExceptionMessage;

    @BeforeAll
    static void setUpAll() throws ParseException
    {
        borrowDate1 = df.parse("2020-10-01");
        borrowDate2 = df.parse("2020-10-05");
        borrowDate3 = df.parse("2020-10-10");
        borrowDate4 = df.parse("2020-10-15");

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
    void borrowBook() throws ParseException
    {
        // If the book is only a reference when borrowing a book,
        // an exception is thrown.
        book1.setReferenceOnly(true);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.checkoutBook(member1, book1, borrowDate1);
        }).getMessage();
        Assertions.assertEquals("Sorry, but this book is only for reference " +
                "and cannot be borrowed.",
                memberExceptionMessage);
        book1.setReferenceOnly(false);

        // If the book is currently lost,
        // an exception is thrown.
        book1.setStatus(BookStatus.LOST);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.checkoutBook(member1, book1, borrowDate1);
        }).getMessage();
        Assertions.assertEquals("Sorry, but the book is lost and " +
                        "cannot be found at the time.",
                memberExceptionMessage);
        book1.setStatus(BookStatus.AVAILABLE);

        // If the member is currently at the limit of books issued and
        // tries to borrow a book, an exception is thrown.
        member1.setIssuedBooksTotal(5);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.checkoutBook(member1, book1, borrowDate1);
        }).getMessage();
        Assertions.assertEquals("Sorry, but the user is currently at the maximum limit " +
                        "on how many books can be issued to them.",
                memberExceptionMessage);
        member1.setIssuedBooksTotal(0);

        // If there are no issues with the member or the book, the
        // member will be able to checkout the book.
        Assertions.assertDoesNotThrow(() -> {
            memberService.checkoutBook(member1, book1, borrowDate1);
        });
        Assertions.assertEquals(member1, book1.getCurrLoanMember());
        Assertions.assertEquals(BookStatus.LOANED, book1.getStatus());
        Assertions.assertEquals(borrowDate1, book1.getBorrowed());
        Assertions.assertEquals(new Date(borrowDate1.getTime() +
                Limitations.MAX_LENDING_DAYS * (1000 * 60 * 60 * 24)),
                book1.getDueDate());
        Assertions.assertEquals(1, member1.getIssuedBooksTotal());

        Assertions.assertDoesNotThrow(() -> {
            memberService.checkoutBook(member2, book2, borrowDate2);
        });
        Assertions.assertEquals(member2, book2.getCurrLoanMember());
        Assertions.assertEquals(BookStatus.LOANED, book2.getStatus());
        Assertions.assertEquals(borrowDate2, book2.getBorrowed());
        Assertions.assertEquals(new Date(borrowDate2.getTime() +
                        Limitations.MAX_LENDING_DAYS * (1000 * 60 * 60 * 24)),
                book2.getDueDate());
        Assertions.assertEquals(1, member2.getIssuedBooksTotal());

        // If another member tries to checkout a book that is already loaned,
        // an exception is thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.checkoutBook(member2, book1, borrowDate2);
        }).getMessage();
        Assertions.assertEquals("Sorry, but this book is " +
                        "currently loaned to another member",
                memberExceptionMessage);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.checkoutBook(member1, book2, borrowDate1);
        }).getMessage();
        Assertions.assertEquals("Sorry, but this book is " +
                        "currently loaned to another member",
                memberExceptionMessage);

        // If a member tries to borrow the book again after it is loaned to them,
        // an exception is thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.checkoutBook(member1, book1, borrowDate1);
        }).getMessage();
        Assertions.assertEquals("This user is already borrowing this book.",
                memberExceptionMessage);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.checkoutBook(member2, book2, borrowDate2);
        }).getMessage();
        Assertions.assertEquals("This user is already borrowing this book.",
                memberExceptionMessage);
    }

    @Test
    @Order(2)
    void reserveBook()
    {
        // If the book is only a reference when reserving a book,
        // an exception is thrown.
        book3.setReferenceOnly(true);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.reserveBook(member1, book3, df.parse("2020-10-03"));
        }).getMessage();
        Assertions.assertEquals("Sorry, but this book is only for reference " +
                        "and cannot be borrowed.",
                memberExceptionMessage);
        book3.setReferenceOnly(false);

        // If the book is currently lost,
        // an exception is thrown.
        book3.setStatus(BookStatus.LOST);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.reserveBook(member1, book3, df.parse("2020-10-03"));
        }).getMessage();
        Assertions.assertEquals("Sorry, but the book is lost and " +
                        "cannot be found at the time.",
                memberExceptionMessage);
        book3.setStatus(BookStatus.AVAILABLE);

        // If the member is currently at the limit of books issued and
        // tries to reserve a book, an exception is thrown.
        int currIssuedBooksTotal = member1.getIssuedBooksTotal();
        member1.setIssuedBooksTotal(5);
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.reserveBook(member1, book3, df.parse("2020-10-03"));
        }).getMessage();
        Assertions.assertEquals("Sorry, but the user is currently at the maximum limit " +
                        "on how many books can be issued to them.",
                memberExceptionMessage);
        member1.setIssuedBooksTotal(currIssuedBooksTotal);

        // If there are no issues with the book or the member,
        // the member can reserve the book.
        Assertions.assertDoesNotThrow(() -> {
            memberService.reserveBook(member1, book3, df.parse("2020-10-03"));
        });
        Assertions.assertEquals(member1, book3.getCurrReservedMember());
        Assertions.assertEquals(BookStatus.RESERVED, book3.getStatus());
        Assertions.assertEquals(2, member1.getIssuedBooksTotal());

        // A member is able to reserve a book that is currently
        // loaned to another member.
        Assertions.assertDoesNotThrow(() -> {
            memberService.reserveBook(member2, book1, df.parse("2020-10-03"));
        });
        Assertions.assertEquals(member2, book1.getCurrReservedMember());
        Assertions.assertEquals(BookStatus.LOANED, book1.getStatus());
        Assertions.assertEquals(2, member2.getIssuedBooksTotal());

        // If the books is currently reserved for another member
        // and a member tries to reserve the book, an exception
        // is thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.reserveBook(member2, book3, df.parse("2020-10-03"));
        }).getMessage();
        Assertions.assertEquals("Sorry, but this book is currently " +
                        "reserved for another member",
                memberExceptionMessage);

        // If a member tries to reserve a book they already reserved,
        // an exception is thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.reserveBook(member1, book3, df.parse("2020-10-03"));
        }).getMessage();
        Assertions.assertEquals("This user has already reserved this book.",
                memberExceptionMessage);

        // If the member has the book reserved and is not currently loaned
        // to another member, then the member can proceed with borrowing the
        // book.
        Assertions.assertDoesNotThrow(() -> {
            memberService.checkoutBook(member1, book3, borrowDate3);
        });
        Assertions.assertEquals(member1, book3.getCurrLoanMember());
        Assertions.assertNull(book3.getCurrReservedMember());
        Assertions.assertEquals(BookStatus.LOANED, book3.getStatus());
        Assertions.assertEquals(2, member1.getIssuedBooksTotal());
    }

    @Test
    @Order(3)
    void returnBook()
    {
        // If a book is currently not loaned to a member and is being returned,
        // an exception is thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () ->
                memberService.returnBook(member1, book4, df.parse("2020-10-10"))).getMessage();
        Assertions.assertEquals("Book cannot be returned as it is not " +
                "currently being loaned to a member.",
                memberExceptionMessage);

        // If a book is not being returned by the same member that it is loaned to,
        // an exception is thrown.
        memberExceptionMessage = Assertions.assertThrows(ApiRequestException.class, () -> {
            memberService.returnBook(member2, book3, df.parse("2020-10-10"));
        }).getMessage();
        Assertions.assertEquals("This book is not issued to this user.",
                memberExceptionMessage);

        // If the book is returned in time by the member, no fine is issued
        // and the book will be available or currently reserved for another
        // member if reserved while it was being loaned.
        Assertions.assertDoesNotThrow(() ->
                memberService.returnBook(member1, book1,
                new Date(borrowDate1.getTime() +
                        5 * (1000 * 60 * 60 * 24))));
        Assertions.assertNull(book1.getCurrLoanMember());
        Assertions.assertEquals(1, member1.getIssuedBooksTotal());

        Assertions.assertEquals(BookStatus.RESERVED, book1.getStatus());
        Assertions.assertEquals(member2, book1.getCurrReservedMember());
        Assertions.assertDoesNotThrow(() ->
                memberService.checkoutBook(member2, book1,
                new Date(borrowDate1.getTime() +
                        5 * (1000 * 60 * 60 * 24))));
        Assertions.assertEquals(BookStatus.LOANED, book1.getStatus());
        Assertions.assertNull(book1.getCurrReservedMember());
        Assertions.assertEquals(member2, book1.getCurrLoanMember());
        Assertions.assertEquals(2, member2.getIssuedBooksTotal());

    }

    @Test
    @Order(4)
    void renewBook()
    {

    }
    @Test
    @Order(5)
    void payFine()
    {

    }
}
