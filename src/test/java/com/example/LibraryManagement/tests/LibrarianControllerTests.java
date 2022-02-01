package com.example.LibraryManagement.tests;

import com.example.LibraryManagement.components.controllers.LibrarianController;
import com.example.LibraryManagement.components.services.ValidationService;
import com.example.LibraryManagement.components.services.accounts.AccountServiceImp;
import com.example.LibraryManagement.components.services.accounts.LibrarianServiceImp;
import com.example.LibraryManagement.components.services.catalogs.UpdateCatalogServiceImp;
import com.example.LibraryManagement.models.accounts.types.Librarian;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.RegisterLibrarianRequest;
import com.example.LibraryManagement.models.io.responses.exceptions.ApiRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration
@SpringBootTest
public class LibrarianControllerTests
{
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private LibrarianController librarianController;

    private String libraryControllerPath = "/library_website";

    @BeforeEach
    void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @AfterEach
    void tearDown()
    {

    }

    @Test
    @Order(1)
    void registerLibrarians() throws Exception
    {
        RegisterLibrarianRequest registerLibrarianRequest = new RegisterLibrarianRequest(
                "testlibrarian",
                "123456",
                "testlibrarian@mail.com",
                "street",
                "city",
                "111222",
                "country",
                "1234567890");

        mockMvc.perform(post(libraryControllerPath +
                "/account/librarian/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerLibrarianRequest)))
                .andExpect(status().is(409));

    }
}
