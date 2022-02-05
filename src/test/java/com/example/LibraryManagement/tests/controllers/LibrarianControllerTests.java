package com.example.LibraryManagement.tests.controllers;

import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.books.properties.BookItem;
import com.example.LibraryManagement.models.enums.books.BookFormat;
import com.example.LibraryManagement.models.io.requests.librarian_requests.SubjectRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.delete.RemoveAuthorRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.delete.RemoveBookItemRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.delete.RemoveLibraryRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.AddAuthorRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.AddBookItemRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.AddLibraryRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.RegisterLibrarianRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.put.MoveBookItemRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.put.UpdateAuthorRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.put.UpdateBookItemRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
public class LibrarianControllerTests
{
    // Properties used for mock testing the application.
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;
    private MockMvc mockMvc;

    // Paths for API requests in controllers.
    private final String librarianControllerPath = "/library_website";
    private final String catalogControllerPath = "/library_website/catalog";

    // Samples used for testing.
    private LibraryCard librarianCard;
    private RegisterLibrarianRequest registerLibrarian;

    @BeforeEach
    void setUp() throws Exception
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        registerLibrarian = new RegisterLibrarianRequest(
                "Manny South",
                "1024MS",
                "librarian@mail.com",
                "librarian's street",
                "librarian's city",
                "444444",
                "US",
                "9543138282");

        MvcResult librarianResult = mockMvc.perform(post(librarianControllerPath +
                "/account/librarian/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerLibrarian)))
                .andExpect(status().is(201))
                .andReturn();

        String result = librarianResult.getResponse().getContentAsString();
        librarianCard = mapper.readValue(result, LibraryCard.class);
    }

    @Test
    void updateAndViewCatalog() throws Exception
    {
        MvcResult mvcResult;
        String result;
        List<BookItem> books;

        AddLibraryRequest addLibraryRequest;
        AddAuthorRequest addAuthorRequest;
        SubjectRequest subjectRequest;
        UpdateBookItemRequest updateBookRequest;
        MoveBookItemRequest moveBookRequest;
        UpdateAuthorRequest updateAuthorRequest;
        RemoveLibraryRequest removeLibraryRequest;
        RemoveAuthorRequest removeAuthorRequest;
        RemoveBookItemRequest removeBookItemRequest;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        List<String> authors = Arrays.asList("David Stuart", "Robert South");
        List<String> subjects = Arrays.asList("Action", "Comedy", "Romance", "Horror", "Drama");
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

        // Check that librarians can add libraries to the system.
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

        addLibraryRequest = new AddLibraryRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "West Library",
                "West Street",
                "West City",
                "320332",
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

        for(String author: authors)
        {
            addAuthorRequest = new AddAuthorRequest(
                    librarianCard.getBarcode(),
                    librarianCard.getCardNumber(),
                    author,
                    "New author");
            mockMvc.perform(post(librarianControllerPath +
                    "/catalog/author/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(addAuthorRequest)))
                    .andExpect(status().is(201));
            mockMvc.perform(post(librarianControllerPath +
                    "/catalog/author/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(addAuthorRequest)))
                    .andExpect(status().is(409));
        }

        for(String subject: subjects)
        {
            subjectRequest = new SubjectRequest(
                    librarianCard.getBarcode(),
                    librarianCard.getCardNumber(),
                    subject);
            mockMvc.perform(post(librarianControllerPath +
                    "/catalog/subject/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(subjectRequest)))
                    .andExpect(status().is(201));
            mockMvc.perform(post(librarianControllerPath +
                    "/catalog/subject/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(subjectRequest)))
                    .andExpect(status().is(409));
        }

        for(AddBookItemRequest addBookRequest: addBookRequests)
        {
            mockMvc.perform(post(librarianControllerPath +
                    "/catalog/book/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(addBookRequest)))
                    .andExpect(status().is(201));
        }

        // Check that catalog displays the right books, libraries, authors,
        // and subjects during search.
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
        books = mapper.readValue(result, new TypeReference<List<BookItem>>() {});

        mockMvc.perform(get(catalogControllerPath +
                "/library")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "East Library", "West Library")));

        mockMvc.perform(get(catalogControllerPath +
                "/subjects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Action", "Comedy", "Romance",
                        "Horror", "Drama", "Suspense")));

        mockMvc.perform(get(catalogControllerPath +
                "/author")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        addBook1Request.getAuthor(),
                        addBook2Request.getAuthor(),
                        addBook3Request.getAuthor())));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("library", "East Library"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        addBook1Request.getTitle(),
                        addBook2Request.getTitle(),
                        addBook3Request.getTitle())));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("library", "West Library"))
                .andExpect(status().is(404));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("author", addBook1Request.getAuthor()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].author.name",
                        is(addBook1Request.getAuthor())));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "Action"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        addBook1Request.getTitle(),
                        addBook2Request.getTitle())));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("subjects", "Action"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        addBook1Request.getTitle(),
                        addBook2Request.getTitle())));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("subjects", "Suspense"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        addBook3Request.getTitle())));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("subjects", "Drama"))
                .andExpect(status().is(404));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pub_date", "2007-03-10"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        addBook3Request.getTitle())));

        // Check that books are updated properly to the expected properties
        // by librarians.
        for(BookItem book: books)
        {
            updateBookRequest = new UpdateBookItemRequest(
                    librarianCard.getBarcode(),
                    librarianCard.getCardNumber(),
                    book.getBarcode(),
                    book.getISBN(),
                    "Drama Book",
                    "New Publisher Company",
                    book.getLanguage(),
                    book.getNumberOfPages(),
                    book.getAuthor().getName(),
                    new HashSet<>(Arrays.asList("Drama")),
                    BookFormat.EBOOK,
                    book.getPublicationDate(),
                    book.isReferenceOnly(),
                    book.getPrice());
            moveBookRequest = new MoveBookItemRequest(
                    librarianCard.getBarcode(),
                    librarianCard.getCardNumber(),
                    book.getBarcode(),
                    "West Library",
                    3,
                    "F");
            mockMvc.perform(put(librarianControllerPath +
                    "/catalog/book/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(updateBookRequest)))
                    .andExpect(status().is(200));
            mockMvc.perform(put(librarianControllerPath +
                    "/catalog/book/move")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(moveBookRequest)))
                    .andExpect(status().is(200));
        }

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("subjects", "Drama"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        "Drama Book", "Drama Book", "Drama Book")))
                .andExpect(jsonPath("$[*].format", containsInAnyOrder(
                        BookFormat.EBOOK.toString(),
                        BookFormat.EBOOK.toString(),
                        BookFormat.EBOOK.toString())));
        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("subjects", "Action", "Comedy"))
                .andExpect(status().is(404));

        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("library", "West Library"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        "Drama Book", "Drama Book", "Drama Book")));
        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("library", "East Library"))
                .andExpect(status().is(404));

        mvcResult = mockMvc.perform(get(catalogControllerPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder(
                        "Drama Book", "Drama Book", "Drama Book")))
                .andReturn();
        result = mvcResult.getResponse().getContentAsString();
        books = mapper.readValue(result, new TypeReference<List<BookItem>>() {});

        for(BookItem book: books)
        {
            Assertions.assertEquals(3, book.getRackNumber());
            Assertions.assertEquals("F", book.getLocationIdentifier());
        }

        // Check that author descriptions can be updated by librarians.
        for(String author: authors)
        {
            updateAuthorRequest = new UpdateAuthorRequest(
                    librarianCard.getBarcode(),
                    librarianCard.getCardNumber(),
                    author,
                    "New description");
            mockMvc.perform(put(librarianControllerPath +
                    "/catalog/author/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(updateAuthorRequest)))
                    .andExpect(status().is(200));
        }

        updateAuthorRequest = new UpdateAuthorRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                addBook3Request.getAuthor(),
                "I am " + addBook3Request.getAuthor());
        mockMvc.perform(put(librarianControllerPath +
                "/catalog/author/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateAuthorRequest)))
                .andExpect(status().is(200));

        mockMvc.perform(get(catalogControllerPath +
                "/author")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(
                        "I am " + addBook3Request.getAuthor(),
                        "New description", "New description")));

        // Check that librarians can remove books, libraries, authors,
        // and subjects from the system. Libraries and authors cannot
        // be removed if there are still books associated to them.
        removeLibraryRequest = new RemoveLibraryRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "East Library");
        mockMvc.perform(delete(librarianControllerPath +
                "/catalog/library/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(removeLibraryRequest)))
                .andExpect(status().is(200));
        removeLibraryRequest = new RemoveLibraryRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "West Library");
        mockMvc.perform(delete(librarianControllerPath +
                "/catalog/library/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(removeLibraryRequest)))
                .andExpect(status().is(409));
        mockMvc.perform(get(catalogControllerPath +
                "/library")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "West Library")));

        removeAuthorRequest = new RemoveAuthorRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                addBook1Request.getAuthor());
        mockMvc.perform(delete(librarianControllerPath +
                "/catalog/author/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(removeAuthorRequest)))
                .andExpect(status().is(409));

        subjectRequest = new SubjectRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "Drama");
        mockMvc.perform(delete(librarianControllerPath +
                "/catalog/subject/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subjectRequest)))
                .andExpect(status().is(200));
        mockMvc.perform(get(catalogControllerPath +
                "/subjects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Action", "Comedy", "Romance",
                        "Horror", "Suspense")));
        mockMvc.perform(get(catalogControllerPath +
                "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("subjects", "Drama"))
                .andExpect(status().is(404));

        for(BookItem book: books)
        {
            removeBookItemRequest = new RemoveBookItemRequest(
                    librarianCard.getBarcode(),
                    librarianCard.getCardNumber(),
                    book.getBarcode());
            removeAuthorRequest = new RemoveAuthorRequest(
                    librarianCard.getBarcode(),
                    librarianCard.getCardNumber(),
                    book.getAuthor().getName());

            mockMvc.perform(delete(librarianControllerPath +
                    "/catalog/book/remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(removeBookItemRequest)))
                    .andExpect(status().is(200));
            mockMvc.perform(delete(librarianControllerPath +
                    "/catalog/book/remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(removeBookItemRequest)))
                    .andExpect(status().is(404));

            mockMvc.perform(delete(librarianControllerPath +
                    "/catalog/author/remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(removeAuthorRequest)))
                    .andExpect(status().is(200));
            mockMvc.perform(delete(librarianControllerPath +
                    "/catalog/author/remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(removeAuthorRequest)))
                    .andExpect(status().is(404));
        }

        mockMvc.perform(get(catalogControllerPath)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
        mockMvc.perform(get(catalogControllerPath +
                "/author")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));

        removeLibraryRequest = new RemoveLibraryRequest(
                librarianCard.getBarcode(),
                librarianCard.getCardNumber(),
                "West Library");
        mockMvc.perform(delete(librarianControllerPath +
                "/catalog/library/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(removeLibraryRequest)))
                .andExpect(status().is(200));
        mockMvc.perform(delete(librarianControllerPath +
                "/catalog/library/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(removeLibraryRequest)))
                .andExpect(status().is(404));
    }
}
