package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.MemberServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * TODO: Add functions for borrowing, reserving, renewing books, as well check their account's details.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping("library_website/member")
public class MemberController
{
    @Autowired
    private final MemberServiceImp memberService;
}
