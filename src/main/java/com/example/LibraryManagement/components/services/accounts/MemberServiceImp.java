package com.example.LibraryManagement.components.services.accounts;

import com.example.LibraryManagement.components.repositories.accounts.AccountNotificationRepository;
import com.example.LibraryManagement.components.repositories.books.BookLendingRepository;
import com.example.LibraryManagement.components.repositories.books.BookReservationRepository;
import com.example.LibraryManagement.components.repositories.fines.*;
import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.actions.BookLending;
import com.example.LibraryManagement.models.books.actions.BookReservation;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.fines.FineTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CashTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CheckTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CreditCardTransaction;
import com.example.LibraryManagement.models.books.notifications.AccountNotification;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.books.properties.Limitations;
import com.example.LibraryManagement.models.enums.books.BookStatus;
import com.example.LibraryManagement.models.enums.fines.TransactionType;
import com.example.LibraryManagement.models.enums.reservations.ReservationStatus;
import com.example.LibraryManagement.models.interfaces.services.accounts.MemberService;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class MemberServiceImp implements MemberService
{
    @Autowired
    private final BookLendingRepository bookLendingRepository;
    @Autowired
    private final BookReservationRepository bookReservationRepository;
    @Autowired
    private final AccountNotificationRepository notificationRepository;
    @Autowired
    private final FineRepository fineRepository;
    @Autowired
    private final FineTransactionRepository fineTransactionRepository;
    @Autowired
    private final CreditCardTransactionRepository creditCardTransactionRepository;
    @Autowired
    private final CheckTransactionRepository checkTransactionRepository;
    @Autowired
    private final CashTransactionRepository cashTransactionRepository;

    @Transactional
    public ResponseEntity<BookItem> checkoutBook(Member member, BookItem book, Date currDate)
    {
        Date dueDate = new Date(currDate.getTime() + Limitations.MAX_LENDING_DAYS * (1000 * 60 * 60 * 24));
        AccountNotification notification = new AccountNotification(member,
                currDate, member.getEmail(), member.getAddress(),
                "User has been loaned the book \"" + book.getTitle() + "\".");
        BookLending bookLoan = new BookLending(book, member, currDate, dueDate);

        if(book.isReferenceOnly())
            throw new ApiRequestException("Sorry, but this book is only for reference " +
                    "and cannot be borrowed.",
                    HttpStatus.CONFLICT);

        else if(member.getIssuedBooksTotal() >= Limitations.MAX_ISSUED_BOOKS)
            throw new ApiRequestException("Sorry, but the user is currently at the maximum limit " +
                    "on how many books can be issued to them.",
                    HttpStatus.CONFLICT);

        else if(book.getStatus() == BookStatus.LOANED && book.getCurrLoanMember() != null)
        {
            Member loanedMember = book.getCurrLoanMember();

            if(!loanedMember.equals(member))
                throw new ApiRequestException("Sorry, but this book is " +
                        "currently loaned to another member",
                        HttpStatus.CONFLICT);

            throw new ApiRequestException("This user is already borrowing this book.",
                    HttpStatus.BAD_REQUEST);
        }

        else if(book.getStatus() == BookStatus.LOST)
            throw new ApiRequestException("Sorry, but the book is lost and " +
                    "cannot be found at the time.",
                    HttpStatus.CONFLICT);

        else if(book.getCurrReservedMember() != null)
        {
            Member reservedMember = book.getCurrReservedMember();

            if(!reservedMember.equals(member))
                throw new ApiRequestException("Sorry, but this book is currently reserved for another member",
                        HttpStatus.CONFLICT);

            bookLendingRepository.save(bookLoan);
            member.checkoutReservedBookItem(book, bookLoan);
            notificationRepository.save(notification);
            member.sendNotification(notification);

            book.setStatus(BookStatus.LOANED);
            book.setBorrowed(currDate);
            book.setDueDate(dueDate);
            book.setCurrLoanMember(member);
            book.setCurrReservedMember(null);
            book.addLoanRecord(bookLoan);

            return ResponseEntity.ok(book);
        }

        bookLendingRepository.save(bookLoan);
        member.checkoutBookItem(book, bookLoan);
        notificationRepository.save(notification);
        member.sendNotification(notification);

        book.setStatus(BookStatus.LOANED);
        book.setBorrowed(currDate);
        book.setDueDate(dueDate);
        book.setCurrLoanMember(member);
        book.addLoanRecord(bookLoan);

        return ResponseEntity.ok(book);
    }

    @Transactional
    public ResponseEntity<MessageResponse> returnBook(Member member, BookItem book, Date currDate)
    {
        Member loanedMember = book.getCurrLoanMember();

        if(book.getStatus() != BookStatus.LOANED || loanedMember == null)
            throw new ApiRequestException("Book cannot be returned as it is not " +
                    "currently being loaned to a member.",
                    HttpStatus.BAD_REQUEST);

        else if(!loanedMember.equals(member))
            throw new ApiRequestException("This book is not issued to this user.",
                    HttpStatus.BAD_REQUEST);

        Date returnDate = currDate;
        Date dueDate = book.getDueDate();
        AccountNotification notification = new AccountNotification(member, returnDate, member.getEmail(), member.getAddress(),
            "User has returned the book \"" + book.getTitle() + "\" on time.");
        MessageResponse response = new MessageResponse("Book has been returned.");

        member.returnBookItem(book, returnDate);

        if(dueDate.compareTo(returnDate) < 0)
        {
            long diffInMillies = Math.abs(returnDate.getTime() - dueDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            double amount = Limitations.FINE_PER_DAY * diff;

            response = new MessageResponse("Book has been returned late. User must pay a fine.");
            AccountNotification fineNotification = new AccountNotification(member,
                    returnDate, member.getEmail(), member.getAddress(),
                    "User has been issued a fine of $" + amount
                            + " for returning the book \"" + book.getTitle()
                            + "\" late.");

            Fine fine = new Fine(amount, member);
            fineRepository.save(fine);

            member.addFine(fine);
            notificationRepository.save(fineNotification);
            member.sendNotification(fineNotification);
        }

        else
        {
            notificationRepository.save(notification);
            member.sendNotification(notification);
        }

        book.setCurrLoanMember(null);
        book.setBorrowed(null);
        book.setDueDate(null);

        if(book.getCurrReservedMember() != null)
        {
            Member reservedMember = book.getCurrReservedMember();
            reservedMember.updatedPendingReservation(book);

            AccountNotification reservationNotification = new AccountNotification(reservedMember,
                    returnDate, reservedMember.getEmail(), reservedMember.getAddress(),
                    "Reservation for book \"" + book.getTitle() + "\" is now available.");

            notificationRepository.save(reservationNotification);
            reservedMember.sendNotification(reservationNotification);
            book.setStatus(BookStatus.RESERVED);
        }

        else
        {
            book.setStatus(BookStatus.AVAILABLE);
        }

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<MessageResponse> reserveBook(Member member, BookItem book, Date currDate)
    {
        AccountNotification notification = new AccountNotification(member, currDate, member.getEmail(), member.getAddress(),
                "User has made a reservation for the book \"" + book.getTitle() + "\".");
        BookReservation bookReservation = new BookReservation(book, member, currDate, ReservationStatus.WAITING);

        if(book.isReferenceOnly())
            throw new ApiRequestException("Sorry, but this book is only for reference and cannot be borrowed.",
                    HttpStatus.CONFLICT);

        else if(member.getIssuedBooksTotal() >= Limitations.MAX_ISSUED_BOOKS)
            throw new ApiRequestException("Sorry, but the user is currently at the " +
                    "maximum limit on how many books can be issued to them.",
                    HttpStatus.CONFLICT);

        else if(book.getCurrReservedMember() != null)
        {
            Member reservedMember = book.getCurrReservedMember();

            if(!reservedMember.equals(member))
                throw new ApiRequestException("Sorry, but this book is currently " +
                        "reserved for another member",
                        HttpStatus.CONFLICT);

            throw new ApiRequestException("This user has already reserved this book.",
                    HttpStatus.BAD_REQUEST);
        }

        else if(book.getStatus() == BookStatus.LOST)
            throw new ApiRequestException("Sorry, but the book is lost and cannot be found at the time.",
                    HttpStatus.CONFLICT);

        else if(book.getStatus() == BookStatus.AVAILABLE)
        {
            book.setStatus(BookStatus.RESERVED);
            bookReservation.setStatus(ReservationStatus.PENDING);
        }

        bookReservationRepository.save(bookReservation);
        member.reserveBookItem(book, bookReservation);
        notificationRepository.save(notification);
        member.sendNotification(notification);

        book.setCurrReservedMember(member);
        book.addReservationRecord(bookReservation);

        return ResponseEntity.ok(new MessageResponse("Book has been reserved for user."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> cancelReservation(Member member, BookItem book, Date currDate)
    {
        Member reservedMember = book.getCurrReservedMember();

        if(book.getStatus() == BookStatus.AVAILABLE ||reservedMember == null)
            throw new ApiRequestException("This book is not being reserved by anyone.",
                    HttpStatus.BAD_REQUEST);

        else if(!reservedMember.equals(member))
            throw new ApiRequestException("This book is being reserved by another user.",
                    HttpStatus.BAD_REQUEST);

        AccountNotification notification = new AccountNotification(member,
                currDate, member.getEmail(), member.getAddress(),
                "User has cancelled their reservation for the book \"" + book.getTitle() + "\".");

        notificationRepository.save(notification);
        member.sendNotification(notification);
        member.cancelReservedBookItem(book);

        if(book.getStatus() == BookStatus.RESERVED)
            book.setStatus(BookStatus.AVAILABLE);

        book.setCurrReservedMember(null);

        return ResponseEntity.ok(new MessageResponse("User has cancelled their reservation for this book."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> renewBook(Member member, BookItem book, Date currDate)
    {
        Member loanedMember = book.getCurrLoanMember();

        if(book.getStatus() != BookStatus.LOANED || loanedMember == null)
            throw new ApiRequestException("Book cannot be renewed as it is not " +
                    "currently being loaned to a member.",
                    HttpStatus.BAD_REQUEST);

        else if(!loanedMember.equals(member))
            throw new ApiRequestException("This book is not issued to this user.",
                    HttpStatus.BAD_REQUEST);

        Date returnDate = currDate;
        Date dueDate = book.getDueDate();
        Date newDueDate = new Date(dueDate.getTime() + Limitations.MAX_LENDING_DAYS * (1000 * 60 * 60 * 24));
        AccountNotification notification = new AccountNotification(member,
                returnDate, member.getEmail(), member.getAddress(),
                "User has renewed the book \"" + book.getTitle() + "\".");

        if(dueDate.compareTo(returnDate) < 0)
        {
            long diffInMillies = Math.abs(returnDate.getTime() - dueDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            double amount = Limitations.FINE_PER_DAY * diff;

            AccountNotification fineNotification = new AccountNotification(member,
                    returnDate, member.getEmail(), member.getAddress(),
                    "User has been issued a fine of " + amount
                            + " for returning the book \"" + book.getTitle()
                            + "\" late.");

            Fine fine = new Fine(amount, member);
            fineRepository.save(fine);

            member.addFine(fine);
            notificationRepository.save(fineNotification);
            member.sendNotification(fineNotification);
            member.returnBookItem(book, currDate);

            book.setCurrLoanMember(null);
            book.setBorrowed(null);
            book.setDueDate(null);

            if(book.getCurrReservedMember() != null)
            {
                Member reservedMember = book.getCurrReservedMember();
                reservedMember.updatedPendingReservation(book);

                AccountNotification reservationNotification = new AccountNotification(reservedMember,
                        returnDate, reservedMember.getEmail(), reservedMember.getAddress(),
                        "Reservation for book \"" + book.getTitle() + "\" is now available.");

                notificationRepository.save(notification);
                reservedMember.sendNotification(reservationNotification);
                book.setStatus(BookStatus.RESERVED);
            }

            else
            {
                book.setStatus(BookStatus.AVAILABLE);
            }

            throw new ApiRequestException("Book has been returned late. " +
                    "User cannot renew the book and must pay a fine.",
                    HttpStatus.CONFLICT);
        }

        else if(book.getCurrReservedMember() != null)
        {
            AccountNotification renewalFailureNotification = new AccountNotification(member,
                    returnDate, member.getEmail(), member.getAddress(),
                    "User was unable to renew the book \"" + book.getTitle() +
                            "\" as it is currently reserved for another member. " +
                            "Book \"" + book.getTitle() + "\" has been returned.");

            notificationRepository.save(renewalFailureNotification);
            member.sendNotification(renewalFailureNotification);
            member.returnBookItem(book, currDate);

            Member reservedMember = book.getCurrReservedMember();
            reservedMember.updatedPendingReservation(book);

            AccountNotification reservationNotification = new AccountNotification(reservedMember,
                    returnDate, reservedMember.getEmail(), reservedMember.getAddress(),
                    "Reservation for book \"" + book.getTitle() + "\" is now available.");

            notificationRepository.save(notification);
            reservedMember.sendNotification(reservationNotification);

            book.setStatus(BookStatus.RESERVED);
            book.setCurrLoanMember(null);
            book.setBorrowed(null);
            book.setDueDate(null);

            throw new ApiRequestException("Book is currently reserved for another member " +
                    "and cannot be renewed.",
                    HttpStatus.CONFLICT);
        }

        member.sendNotification(notification);
        member.renewBookItem(book, dueDate, newDueDate);
        book.setDueDate(newDueDate);
        return ResponseEntity.ok(new MessageResponse("Book has been renewed for the user."));
    }

    @Transactional
    public ResponseEntity<MessageResponse> payFine(Member member, Fine fine, TransactionType type,
                                                   Object transaction, double amount, Date currDate)
    {
        if(!fine.getMember().equals(member))
            throw new ApiRequestException("Fine is not issued to this user.",
                    HttpStatus.BAD_REQUEST);

        else if(fine.isPaid())
            throw new ApiRequestException("Fine has already been paid by the user.",
                    HttpStatus.BAD_REQUEST);

        else if(fine.getAmount() > amount)
            throw new ApiRequestException("Given amount is not enough to pay for the fine.",
                    HttpStatus.UNPROCESSABLE_ENTITY);

        FineTransaction fineTransaction = new FineTransaction(type, currDate, amount);

        switch (type)
        {
            case CREDIT_CARD:
                if(transaction instanceof CreditCardTransaction)
                {
                    CreditCardTransaction cardTransaction = (CreditCardTransaction) transaction;
                    cardTransaction.setFineTransaction(fineTransaction);
                    creditCardTransactionRepository.save(cardTransaction);
                    fineTransaction.setCreditCardTransaction(cardTransaction);
                }

                else
                    throw new ApiRequestException("Unexpected transaction made.",
                            HttpStatus.UNPROCESSABLE_ENTITY);

                break;

            case CHECK:
                if(transaction instanceof CheckTransaction)
                {
                    CheckTransaction checkTransaction = (CheckTransaction) transaction;
                    checkTransaction.setFineTransaction(fineTransaction);
                    checkTransactionRepository.save(checkTransaction);
                    fineTransaction.setCheckTransaction(checkTransaction);
                }

                else
                    throw new ApiRequestException("Unexpected transaction made.",
                            HttpStatus.UNPROCESSABLE_ENTITY);

                break;

            case CASH:
                if(transaction instanceof CashTransaction)
                {
                    CashTransaction cashTransaction = (CashTransaction) transaction;
                    cashTransaction.setFineTransaction(fineTransaction);
                    cashTransactionRepository.save(cashTransaction);
                    fineTransaction.setCashTransaction(cashTransaction);
                }

                else
                    throw new ApiRequestException("Unexpected transaction made.",
                            HttpStatus.UNPROCESSABLE_ENTITY);

                break;

            default:
                throw new ApiRequestException("Transaction cannot be used.",
                        HttpStatus.UNPROCESSABLE_ENTITY);
        }

        fineTransactionRepository.save(fineTransaction);
        fineTransaction.setFine(fine);
        fine.setFineTransaction(fineTransaction);
        fine.setPaid(true);
        member.payFine();

        return ResponseEntity.ok(new MessageResponse("User has successfully paid their fine."));
    }
}
