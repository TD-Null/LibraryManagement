package com.example.LibraryManagement.tests.controllers;

import com.example.LibraryManagement.components.controllers.AccountController;
import com.example.LibraryManagement.components.controllers.CatalogController;
import com.example.LibraryManagement.components.controllers.LibrarianController;
import com.example.LibraryManagement.components.controllers.MemberController;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.io.requests.LoginRequest;
import com.example.LibraryManagement.models.io.requests.SignupRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.RegisterLibrarianRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration
@SpringBootTest
public class ControllerTests
{
    // Properties used for mock testing the application.
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;
    private MockMvc mockMvc;

    // Controllers being used for testing.
    @Autowired
    private AccountController accountController;
    @Autowired
    private LibrarianController librarianController;
    @Autowired
    private MemberController memberController;
    @Autowired
    private CatalogController catalogController;

    // Paths for API requests in controllers.
    private String accountControllerPath = "/library_website/account";
    private String libraryControllerPath = "/library_website";
    private String memberControllerPath = "/library_website/account/member";
    private String catalogControllerPath = "/library_website/catalog";

    // Samples used for testing.
    private LibraryCard memberCard;
    private LibraryCard librarianCard;
    private SignupRequest registerMember;
    private RegisterLibrarianRequest registerLibrarian;

    @BeforeEach
    void setUp() throws Exception
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        registerMember = new SignupRequest(
                "Daniel Manning",
                "0824DM",
                "user@mail.com",
                "user's street",
                "user's city",
                "111111",
                "US",
                "9541087310");

        registerLibrarian = new RegisterLibrarianRequest(
                "Manny South",
                "1024MS",
                "librarian@mail.com",
                "librarian's street",
                "librarian's city",
                "444444",
                "US",
                "9543138282");

        MvcResult memberResult = mockMvc.perform(post(accountControllerPath +
                "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerMember)))
                .andExpect(status().is(200))
                .andReturn();

        MvcResult librarianResult = mockMvc.perform(post(libraryControllerPath +
                "/account/librarian/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerLibrarian)))
                .andExpect(status().is(200))
                .andReturn();

        String result = memberResult.getResponse().getContentAsString();
        memberCard = mapper.readValue(result, LibraryCard.class);

        result = librarianResult.getResponse().getContentAsString();
        librarianCard = mapper.readValue(result, LibraryCard.class);
    }

    @Test
    @Order(1)
    void registerAccounts() throws Exception
    {
        MvcResult mvcResult;
        String result;
        LoginRequest loginRequest;

        loginRequest = new LoginRequest(memberCard.getCardNumber(), registerMember.getPassword());
        mvcResult = mockMvc.perform(post(accountControllerPath +
                "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(200))
                .andReturn();

        result = mvcResult.getResponse().getContentAsString();
        LibraryCard cardResult = mapper.readValue(result, LibraryCard.class);
        Assertions.assertEquals(memberCard.getBarcode(), cardResult.getBarcode());
        Assertions.assertEquals(memberCard.getMember(), cardResult.getMember());

    }
}
