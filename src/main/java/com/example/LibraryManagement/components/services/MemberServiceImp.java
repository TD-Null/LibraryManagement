package com.example.LibraryManagement.components.services;

import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.interfaces.services.accounts.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;

@AllArgsConstructor
@Service
public class MemberServiceImp implements MemberService
{
    @Autowired
    private final MemberRepository memberRepository;


}
