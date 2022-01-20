package com.example.LibraryManagement.tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceImpTests
{
    @BeforeEach
    void setUp()
    {

    }


    @BeforeAll
    static void setUpAll()
    {

    }

    @AfterAll
    static void tearDownAll() throws Exception
    {
    }

    @Test
    @Order(1)
    void registerAccounts()
    {

    }

    @Test
    @Order(2)
    void updateAccounts() {
    }
}