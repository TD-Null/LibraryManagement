package com.example.LibraryManagement.tests.controllers;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.requests.CardValidationRequest;
import com.example.LibraryManagement.models.io.requests.SignupRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.AddBookItemRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.AddLibraryRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.RegisterLibrarianRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class MemberControllerTests
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
    private final String catalogControllerPath = "/library_website/catalog";

    // Samples used for testing.
    private LibraryCard memberCard1;
    private LibraryCard memberCard2;
    private LibraryCard librarianCard;
    private SignupRequest registerMember1;
    private SignupRequest registerMember2;
    private RegisterLibrarianRequest registerLibrarian;

    @BeforeEach
    void setUp() throws Exception
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        registerMember1 = new SignupRequest(
                "Daniel Manning",
                "0824DM",
                "user1@mail.com",
                "user1's street",
                "user1's city",
                "111111",
                "US",
                "9541087310");

        registerMember2 = new SignupRequest(
                "Howard Stuart",
                "1462HS",
                "user2@mail.com",
                "user2's street",
                "user2's city",
                "222222",
                "US",
                "9541147609");

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
                .content(mapper.writeValueAsString(registerMember1)))
                .andExpect(status().is(201))
                .andReturn();

        String result = memberResult.getResponse().getContentAsString();
        memberCard1 = mapper.readValue(result, LibraryCard.class);

        memberResult = mockMvc.perform(post(accountControllerPath +
                "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerMember2)))
                .andExpect(status().is(201))
                .andReturn();

        result = memberResult.getResponse().getContentAsString();
        memberCard2 = mapper.readValue(result, LibraryCard.class);

        MvcResult librarianResult = mockMvc.perform(post(librarianControllerPath +
                "/account/librarian/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerLibrarian)))
                .andExpect(status().is(201))
                .andReturn();

        result = librarianResult.getResponse().getContentAsString();
        librarianCard = mapper.readValue(result, LibraryCard.class);
    }

    @Test
    void booksExchange() throws Exception {
        MvcResult mvcResult;
        String result;
        List<BookItem> books;
        CardValidationRequest cardValidationRequest;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        AddLibraryRequest addLibraryRequest;
        AddBookItemRequest addBook1Request = new AddBookItemRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "East Library",
                1,
                "E",
                "921-1-90-113401-2",
                "Action Comedy Book",
                "Publisher company",
                "English",
                100,
                "David Stuart",
                new HashSet<>(Arrays.asList("Action", "Comedy")),
                BookFormat.HARDCOVER,
                df.parse("2010-10-01"),
                false,
                10.0);
        AddBookItemRequest addBook2Request = new AddBookItemRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "East Library",
                1,
                "E",
                "812-5-57-150290-3",
                "Action Romance Book",
                "Publisher company",
                "English",
                250,
                "Robert South",
                new HashSet<>(Arrays.asList("Action", "Romance")),
                BookFormat.HARDCOVER,
                df.parse("2005-05-20"),
                false,
                5.0);
        AddBookItemRequest addBook3Request = new AddBookItemRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "East Library",
                1,
                "E",
                "902-66-2-033278-7",
                "Suspense Book",
                "Publisher company",
                "English",
                50,
                "Susan North",
                new HashSet<>(Arrays.asList("Suspense")),
                BookFormat.HARDCOVER,
                df.parse("2007-03-10"),
                false,
                15.0);
        List<AddBookItemRequest> addBookRequests = Arrays.asList(
                addBook1Request, addBook2Request, addBook3Request);

        addLibraryRequest = new AddLibraryRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "East Library",
                "East Street",
                "East City",
                "332951",
                "US");
        mockMvc.perform(post(librarianControllerPath +
                "/catalog/library/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addLibraryRequest)))
                .andExpect(status().is(201));
        mockMvc.perform(post(librarianControllerPath +
                "/catalog/library/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addLibraryRequest)))
                .andExpect(status().is(409));

        for (AddBookItemRequest addBookRequest : addBookRequests) {
            mockMvc.perform(post(librarianControllerPath +
                    "/catalog/book/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(addBookRequest)))
                    .andExpect(status().is(201));
        }

        mvcResult = mockMvc.perform(get(catalogControllerPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        addBook1Request.getTitle(),
                        addBook2Request.getTitle(),
                        addBook3Request.getTitle())))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        books = mapper.readValue(result, new TypeReference<List<BookItem>>() {
        });

        /*
         * Member accounts are able to borrow books within the system.
         * Members cannot borrow books that have been loaned to another member
         * and cannot borrow books they have already borrowed. Members can
         * then view the current books they have borrowed and even records
         * and notifications of borrowing books.
         */
        cardValidationRequest = new CardValidationRequest(
                memberCard1.getBarcode(),
                memberCard1.getCardNumber());
        mockMvc.perform(put(memberControllerPath +
                "/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(0).getBarcode().toString()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.title",
                        is(books.get(0).getTitle())));
        mockMvc.perform(put(memberControllerPath +
                "/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(0).getBarcode().toString()))
                .andExpect(status().is(400));
        mockMvc.perform(get(memberControllerPath +
                "/checkout/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard1.getBarcode().toString())
                .param("card", memberCard1.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].title",
                        is(books.get(0).getTitle())));
        mockMvc.perform(get(memberControllerPath +
                "/checkout/history")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard1.getBarcode().toString())
                .param("card", memberCard1.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)));
        mockMvc.perform(get(memberControllerPath +
                "/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard1.getBarcode().toString())
                .param("card", memberCard1.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)));

        cardValidationRequest = new CardValidationRequest(
                memberCard2.getBarcode(),
                memberCard2.getCardNumber());
        mockMvc.perform(put(memberControllerPath +
                "/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(0).getBarcode().toString()))
                .andExpect(status().is(409));

        /*
         * Members can also reserve books, especially when they are
         * loaned to another member. Members cannot borrow books
         * that have been reserved for another member, and members
         * cannot reserve books that they have already reserved.
         */
        cardValidationRequest = new CardValidationRequest(
                memberCard2.getBarcode(),
                memberCard2.getCardNumber());
        mockMvc.perform(put(memberControllerPath +
                "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(0).getBarcode().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(put(memberControllerPath +
                "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(0).getBarcode().toString()))
                .andExpect(status().is(400));

        mockMvc.perform(put(memberControllerPath +
                "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(1).getBarcode().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(put(memberControllerPath +
                "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(1).getBarcode().toString()))
                .andExpect(status().is(400));

        mockMvc.perform(put(memberControllerPath +
                "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(2).getBarcode().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(put(memberControllerPath +
                "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(2).getBarcode().toString()))
                .andExpect(status().is(400));

        mockMvc.perform(get(memberControllerPath +
                "/reserve/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard2.getBarcode().toString())
                .param("card", memberCard2.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        books.get(0).getTitle(),
                        books.get(1).getTitle(),
                        books.get(2).getTitle())));
        mockMvc.perform(get(memberControllerPath +
                "/reserve/history")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard2.getBarcode().toString())
                .param("card", memberCard2.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)));
        mockMvc.perform(get(memberControllerPath +
                "/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard2.getBarcode().toString())
                .param("card", memberCard2.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)));

        cardValidationRequest = new CardValidationRequest(
                memberCard1.getBarcode(),
                memberCard1.getCardNumber());
        mockMvc.perform(put(memberControllerPath +
                "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(1).getBarcode().toString()))
                .andExpect(status().is(409));
        mockMvc.perform(put(memberControllerPath +
                "/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(2).getBarcode().toString()))
                .andExpect(status().is(409));
        mockMvc.perform(get(memberControllerPath +
                "/reserve/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard1.getBarcode().toString())
                .param("card", memberCard1.getCardNumber()))
                .andExpect(status().is(404));

        /*
         * Members can then borrow books that they have already
         * reserved, or cancel their reservation when they no
         * longer want the book reserved to them. Members still
         * cannot borrow books they have reserved if it is still
         * loaned to another member.
         */
        cardValidationRequest = new CardValidationRequest(
                memberCard2.getBarcode(),
                memberCard2.getCardNumber());
        mockMvc.perform(put(memberControllerPath +
                "/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(0).getBarcode().toString()))
                .andExpect(status().is(409));
        mockMvc.perform(put(memberControllerPath +
                "/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(1).getBarcode().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(put(memberControllerPath +
                "/reserve/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardValidationRequest))
                .param("book", books.get(2).getBarcode().toString()))
                .andExpect(status().is(200));

        mockMvc.perform(get(memberControllerPath +
                "/reserve/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard2.getBarcode().toString())
                .param("card", memberCard2.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)));
        mockMvc.perform(get(memberControllerPath +
                "/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard2.getBarcode().toString())
                .param("card", memberCard2.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(5)));
        mockMvc.perform(get(memberControllerPath +
                "/checkout/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard2.getBarcode().toString())
                .param("card", memberCard2.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].title",
                        is(books.get(1).getTitle())));

        /*
         * Members can then return books that they have borrowed.
         * THe book must have a loaned member in order to be returned
         * and must be the right loaned member to return it.
         */
    }
}
