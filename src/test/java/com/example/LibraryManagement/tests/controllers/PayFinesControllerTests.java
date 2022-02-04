package com.example.LibraryManagement.tests.controllers;

import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.repositories.fines.FineRepository;
import com.example.LibraryManagement.components.services.accounts.MemberServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.properties.Limitations;
import com.example.LibraryManagement.models.io.requests.SignupRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.RegisterLibrarianRequest;
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

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
public class PayFinesControllerTests
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

    // Repositories used for testing.
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FineRepository fineRepository;

    // Samples used for testing.
    private LibraryCard memberCard;
    private SignupRequest registerMember;

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

        MvcResult memberResult = mockMvc.perform(post(accountControllerPath +
                "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerMember)))
                .andExpect(status().is(201))
                .andReturn();

        String result = memberResult.getResponse().getContentAsString();
        memberCard = mapper.readValue(result, LibraryCard.class);
    }

    @Test
    @Transactional
    void payFines() throws Exception
    {
        Member member = memberRepository.findMemberByEmail(registerMember.getEmail()).get();

        Fine fine1 = new Fine(Limitations.FINE_PER_DAY * 4, member);
        Fine fine2 = new Fine(Limitations.FINE_PER_DAY * 2, member);
        Fine fine3 = new Fine(Limitations.FINE_PER_DAY * 1, member);
        fineRepository.save(fine1);
        fineRepository.save(fine2);
        fineRepository.save(fine3);

        member.addFine(fine1);
        member.addFine(fine2);
        member.addFine(fine3);
    }
}
