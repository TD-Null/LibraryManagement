package com.example.LibraryManagement.components.controllers;

import com.example.LibraryManagement.components.services.LibrarianServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * TODO: Add functions for adding and modifying book items, blocking members, and checking other member accounts.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping("library_website/librarian")
public class LibrarianController
{
    @Autowired
    private final LibrarianServiceImp librarianService;
}
