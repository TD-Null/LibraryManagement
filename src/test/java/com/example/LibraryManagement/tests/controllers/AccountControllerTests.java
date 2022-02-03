package com.example.LibraryManagement.tests.controllers;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.enums.accounts.AccountStatus;
import com.example.LibraryManagement.models.io.requests.*;
import com.example.LibraryManagement.models.io.requests.librarian_requests.delete.RemoveLibrarianRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.AddLibrarianRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.RegisterLibrarianRequest;
import com.example.LibraryManagement.models.io.requests.member_requests.CancelMembershipRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class AccountControllerTests
{
    // Properties used for mock testing the application.
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;
    private MockMvc mockMvc;

    // Paths for API requests in controllers.
    private final String accountControllerPath = "/library_website/account";
    private final String librarianControllerPath = "/library_website";
    private final String memberControllerPath = "/library_website/account/member";

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
                .andExpect(status().is(201))
                .andReturn();

        MvcResult librarianResult = mockMvc.perform(post(librarianControllerPath +
                "/account/librarian/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerLibrarian)))
                .andExpect(status().is(201))
                .andReturn();

        String result = memberResult.getResponse().getContentAsString();
        memberCard = mapper.readValue(result, LibraryCard.class);

        result = librarianResult.getResponse().getContentAsString();
        librarianCard = mapper.readValue(result, LibraryCard.class);
    }

    @Test
    void userAccounts() throws Exception
    {
        MvcResult mvcResult;
        String result;
        LibraryCard cardResult;
        Member memberAccount;
        Librarian librarianAccount;
        CardValidationRequest cardValidationRequest;
        LoginRequest loginRequest;
        UpdateAccountRequest updateAccountRequest;
        ChangePasswordRequest changePasswordRequest;
        AddLibrarianRequest addLibrarianRequest;
        RemoveLibrarianRequest removeLibrarianRequest;
        CancelMembershipRequest cancelMembershipRequest;

        // Check that the accounts are already registered by logging into the created accounts.
        loginRequest = new LoginRequest(memberCard.getCardNumber(), registerMember.getPassword());
        mvcResult = mockMvc.perform(post(accountControllerPath +
                "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(200))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        cardResult = mapper.readValue(result, LibraryCard.class);
        Assertions.assertEquals(memberCard.getBarcode(), cardResult.getBarcode());
        Assertions.assertEquals(memberCard.getMember(), cardResult.getMember());
        Assertions.assertNull(cardResult.getLibrarian());
        Assertions.assertEquals(memberCard.getType(), cardResult.getType());

        loginRequest = new LoginRequest(librarianCard.getCardNumber(), registerLibrarian.getPassword());
        mvcResult = mockMvc.perform(post(accountControllerPath +
                "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(200))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        cardResult = mapper.readValue(result, LibraryCard.class);
        Assertions.assertEquals(librarianCard.getBarcode(), cardResult.getBarcode());
        Assertions.assertEquals(librarianCard.getLibrarian(), cardResult.getMember());
        Assertions.assertNull(cardResult.getMember());
        Assertions.assertEquals(librarianCard.getType(), cardResult.getType());

        // Check that accounts cannot be registered if the same email is already registered
        // in the database.
        mockMvc.perform(post(accountControllerPath +
                "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerMember)))
                .andExpect(status().is(409));

        mockMvc.perform(post(librarianControllerPath +
                "/account/librarian/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerLibrarian)))
                .andExpect(status().is(409));

        // Check the account details match with what is expected.
        mvcResult = mockMvc.perform(get(accountControllerPath +
                "/details")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard.getBarcode().toString())
                .param("card", memberCard.getCardNumber()))
                .andExpect(status().is(200))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        memberAccount = mapper.readValue(result, Member.class);
        Assertions.assertEquals(memberAccount.getName(), registerMember.getName());
        Assertions.assertEquals(memberAccount.getPassword(), registerMember.getPassword());
        Assertions.assertEquals(memberAccount.getEmail(), registerMember.getEmail());

        mvcResult = mockMvc.perform(get(accountControllerPath +
                "/details")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        librarianAccount = mapper.readValue(result, Librarian.class);
        Assertions.assertEquals(librarianAccount.getName(), registerLibrarian.getName());
        Assertions.assertEquals(librarianAccount.getPassword(), registerLibrarian.getPassword());
        Assertions.assertEquals(librarianAccount.getEmail(), registerLibrarian.getEmail());

        // Check that changes to the accounts are reflected in the database.
        updateAccountRequest = new UpdateAccountRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                "Daniel Manning",
                "user's street",
                "user's city",
                "111111",
                "US",
                "danny@mail.com",
                "9541087310");
        mockMvc.perform(put(accountControllerPath +
                "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateAccountRequest)))
                .andExpect(status().is(200));

        changePasswordRequest = new ChangePasswordRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                memberAccount.getPassword(),
                "password");
        mockMvc.perform(put(accountControllerPath +
                "/update/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().is(200));

        mvcResult = mockMvc.perform(get(accountControllerPath +
                "/details")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard.getBarcode().toString())
                .param("card", memberCard.getCardNumber()))
                .andExpect(status().is(200))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        memberAccount = mapper.readValue(result, Member.class);
        Assertions.assertEquals(memberAccount.getPassword(), changePasswordRequest.getNewPassword());
        Assertions.assertEquals(memberAccount.getEmail(), updateAccountRequest.getEmail());

        updateAccountRequest = new UpdateAccountRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "Manny South",
                "librarian's street",
                "librarian's city",
                "444444",
                "US",
                "manny@mail.com",
                "9543138282");
        mockMvc.perform(put(accountControllerPath +
                "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateAccountRequest)))
                .andExpect(status().is(200));

        changePasswordRequest = new ChangePasswordRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                librarianAccount.getPassword(),
                "password");
        mockMvc.perform(put(accountControllerPath +
                "/update/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().is(200));

        mvcResult = mockMvc.perform(get(accountControllerPath +
                "/details")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        librarianAccount = mapper.readValue(result, Librarian.class);
        Assertions.assertEquals(librarianAccount.getPassword(), changePasswordRequest.getNewPassword());
        Assertions.assertEquals(librarianAccount.getEmail(), updateAccountRequest.getEmail());

        // Check that librarians are able to view both members nad librarian accounts
        // within the system.
        mockMvc.perform(get(librarianControllerPath +
                "/account/member")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(memberAccount.getName())))
                .andExpect(jsonPath("$[0].password", is(memberAccount.getPassword())))
                .andExpect(jsonPath("$[0].email", is(memberAccount.getEmail())))
                .andExpect(jsonPath("$[0].status", is(AccountStatus.ACTIVE.toString())));
        mockMvc.perform(get(librarianControllerPath +
                "/account/librarian")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(librarianAccount.getName())))
                .andExpect(jsonPath("$[0].password", is(librarianAccount.getPassword())))
                .andExpect(jsonPath("$[0].email", is(librarianAccount.getEmail())))
                .andExpect(jsonPath("$[0].status", is(AccountStatus.ACTIVE.toString())));

        // Check if librarians are also able to add other librarians to the system.
        addLibrarianRequest = new AddLibrarianRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "David Stuart",
                "1309DS",
                "librarian2@mail.com",
                "user2's street",
                "user2's city",
                "222222",
                "US",
                "9543510389");
        mvcResult = mockMvc.perform(post(librarianControllerPath +
                "/account/librarian/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addLibrarianRequest)))
                .andExpect(status().is(201))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        LibraryCard newLibrarianCard = mapper.readValue(result, LibraryCard.class);

        loginRequest = new LoginRequest(newLibrarianCard.getCardNumber(), addLibrarianRequest.getPassword());
        mvcResult = mockMvc.perform(post(accountControllerPath +
                "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(200))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        cardResult = mapper.readValue(result, LibraryCard.class);
        Assertions.assertEquals(newLibrarianCard.getBarcode(), cardResult.getBarcode());
        Assertions.assertEquals(newLibrarianCard.getLibrarian(), cardResult.getMember());
        Assertions.assertNull(cardResult.getMember());
        Assertions.assertEquals(newLibrarianCard.getType(), cardResult.getType());

        mockMvc.perform(get(librarianControllerPath +
                "/account/librarian")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(librarianAccount.getName(),
                        addLibrarianRequest.getName())))
                .andExpect(jsonPath("$[*].password", containsInAnyOrder(librarianAccount.getPassword(),
                        addLibrarianRequest.getPassword())))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder(librarianAccount.getEmail(),
                        addLibrarianRequest.getEmail())))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(
                        AccountStatus.ACTIVE.toString(),
                        AccountStatus.ACTIVE.toString())));

        // If the user adding a new librarian account to the system is not identified
        // as a librarian, then the new librarian account will not be added.
        addLibrarianRequest = new AddLibrarianRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                "David Stuart",
                "1309DS",
                "librarian2@mail.com",
                "user2's street",
                "user2's city",
                "222222",
                "US",
                "9543510389");
        mockMvc.perform(post(librarianControllerPath +
                "/account/librarian/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addLibrarianRequest)))
                .andExpect(status().is(401));

        // A librarian can also remove the account from the system. Librarian
        // accounts cannot be removed once their account has been cancelled and
        // cannot login with their credentials.
        removeLibrarianRequest = new RemoveLibrarianRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                newLibrarianCard.getBarcode(),
                newLibrarianCard.getCardNumber());
        mockMvc.perform(delete(librarianControllerPath +
                "/account/librarian/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(removeLibrarianRequest)))
                .andExpect(status().is(200));
        mockMvc.perform(delete(librarianControllerPath +
                "/account/librarian/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(removeLibrarianRequest)))
                .andExpect(status().is(400));
        mockMvc.perform(get(librarianControllerPath +
                "/account/librarian")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(
                        AccountStatus.ACTIVE.toString(),
                        AccountStatus.CANCELLED.toString())));
        loginRequest = new LoginRequest(newLibrarianCard.getCardNumber(), addLibrarianRequest.getPassword());
        mockMvc.perform(post(accountControllerPath +
                "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(401));

        // Check that librarians can block and unblock members.
        cardValidationRequest = new CardValidationRequest(
                librarianCard.getBarcode(), librarianCard.getCardNumber());

        mockMvc.perform(put(librarianControllerPath +
                "/account/member/block")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("member", memberAccount.getId().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(put(librarianControllerPath +
                "/account/member/block")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("member", memberAccount.getId().toString()))
                .andExpect(status().is(403));
        mockMvc.perform(get(accountControllerPath +
                "/details")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard.getBarcode().toString())
                .param("card", memberCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.status", is(AccountStatus.BLACKLISTED.toString())));

        mockMvc.perform(put(librarianControllerPath +
                "/account/member/unblock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("member", memberAccount.getId().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(put(librarianControllerPath +
                "/account/member/unblock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("member", memberAccount.getId().toString()))
                .andExpect(status().is(403));
        mockMvc.perform(get(accountControllerPath +
                "/details")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard.getBarcode().toString())
                .param("card", memberCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.status", is(AccountStatus.ACTIVE.toString())));

        // Check that members can cancel their accounts. Members must input valid
        // credentials before cancelling their account.
        //
        // Members cannot cancel their accounts again and cannot login with their
        // credentials after cancelling their membership.
        cancelMembershipRequest = new CancelMembershipRequest(
                memberCard.getBarcode(), memberCard.getCardNumber(), memberAccount.getPassword());
        mockMvc.perform(delete(memberControllerPath +
                "/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cancelMembershipRequest)))
                .andExpect(status().is(200));
        mockMvc.perform(delete(memberControllerPath +
                "/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cancelMembershipRequest)))
                .andExpect(status().is(400));
        mockMvc.perform(get(librarianControllerPath +
                "/account/member")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].status", is(AccountStatus.CANCELLED.toString())));
        loginRequest = new LoginRequest(memberCard.getCardNumber(), memberAccount.getPassword());
        mockMvc.perform(post(accountControllerPath +
                "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(401));
    }
}
