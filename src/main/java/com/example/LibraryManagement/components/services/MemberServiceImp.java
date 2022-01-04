package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.interfaces.services.accounts.MemberService;
import com.example.LibraryManagement.models.io.responses.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MemberServiceImp implements MemberService
{
    @Autowired
    private final MemberRepository memberRepository;

    public ResponseEntity<BookItem> checkoutBook(Member member, BookItem book)
    {
        return null;
    }

    public ResponseEntity<MessageResponse> returnBook(Member member, BookItem book)
    {
        return null;
    }

    public ResponseEntity<MessageResponse> reserveBook(Member member, BookItem book)
    {
        return null;
    }

    public ResponseEntity<MessageResponse> cancelReservation(Member member, BookItem book)
    {
        return null;
    }

    public ResponseEntity<MessageResponse> renewBook(Member member, BookItem book)
    {
        return null;
    }

    public ResponseEntity<MessageResponse> payFine(Member member, Long fineID)
    {
        return null;
    }
}
