package com.example.LibraryManagement.tests.controllers;

import com.example.LibraryManagement.components.repositories.accounts.MemberRepository;
import com.example.LibraryManagement.components.repositories.fines.FineRepository;
import com.example.LibraryManagement.components.services.accounts.MemberServiceImp;
import com.example.LibraryManagement.models.accounts.LibraryCard;
import com.example.LibraryManagement.models.accounts.types.Member;
import com.example.LibraryManagement.models.books.fines.Fine;
import com.example.LibraryManagement.models.books.fines.transactions.CashTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CheckTransaction;
import com.example.LibraryManagement.models.books.fines.transactions.CreditCardTransaction;
import com.example.LibraryManagement.models.books.properties.Limitations;
import com.example.LibraryManagement.models.enums.fines.TransactionType;
import com.example.LibraryManagement.models.io.requests.SignupRequest;
import com.example.LibraryManagement.models.io.requests.librarian_requests.post.RegisterLibrarianRequest;
import com.example.LibraryManagement.models.io.requests.member_requests.CardTransactionRequest;
import com.example.LibraryManagement.models.io.requests.member_requests.CashTransactionRequest;
import com.example.LibraryManagement.models.io.requests.member_requests.CheckTransactionRequest;
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

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @Transactional
    void payFines() throws Exception
    {
        CardTransactionRequest cardTransactionRequest;
        CheckTransactionRequest checkTransactionRequest;
        CashTransactionRequest cashTransactionRequest;

        /*
         * Check that when fines are issued to a member, the fines are visible
         * within the system by the member who was the issued the fines and the
         * librarian who can view all fines within the system.
         */
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

        mockMvc.perform(get(memberControllerPath +
                "/fines")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard.getBarcode().toString())
                .param("card", memberCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].paid", containsInAnyOrder(
                        false, false, false)));
        mockMvc.perform(get(librarianControllerPath +
                "/records/fines")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].paid", containsInAnyOrder(
                        false, false, false)));

        /*
         * Members can pay for the fines through different types of transactions:
         *
         * - Credit Card
         * - Check
         * - Cash
         *
         * Members must pay within the amount of the fine and will fail to pay
         * the fine if they pay less than the amount. Members also cannot pay
         * for a fine if it has already been paid for.
         */
        cardTransactionRequest = new CardTransactionRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                registerMember.getName(),
                0);
        checkTransactionRequest = new CheckTransactionRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                "Bank of America",
                "108-065-1278",
                0);
        cashTransactionRequest = new CashTransactionRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                0);
        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardTransactionRequest))
                .param("fine", fine1.getId().toString()))
                .andExpect(status().is(422));
        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(checkTransactionRequest))
                .param("fine", fine2.getId().toString()))
                .andExpect(status().is(422));
        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/cash")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cashTransactionRequest))
                .param("fine", fine3.getId().toString()))
                .andExpect(status().is(422));

        cardTransactionRequest = new CardTransactionRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                registerMember.getName(),
                fine1.getAmount());
        checkTransactionRequest = new CheckTransactionRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                "Bank of America",
                "108-065-1278",
                fine2.getAmount());
        cashTransactionRequest = new CashTransactionRequest(
                memberCard.getBarcode(),
                memberCard.getCardNumber(),
                fine3.getAmount());

        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardTransactionRequest))
                .param("fine", fine1.getId().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(checkTransactionRequest))
                .param("fine", fine2.getId().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/cash")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cashTransactionRequest))
                .param("fine", fine3.getId().toString()))
                .andExpect(status().is(200));
        mockMvc.perform(get(memberControllerPath +
                "/fines")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard.getBarcode().toString())
                .param("card", memberCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].paid", containsInAnyOrder(
                        true, true, true)));
        mockMvc.perform(get(librarianControllerPath +
                "/records/fines")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", librarianCard.getBarcode().toString())
                .param("card", librarianCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].paid", containsInAnyOrder(
                        true, true, true)));
        mockMvc.perform(get(memberControllerPath +
                "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("barcode", memberCard.getBarcode().toString())
                .param("card", memberCard.getCardNumber()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(
                        TransactionType.CREDIT_CARD.toString(),
                        TransactionType.CHECK.toString(),
                        TransactionType.CASH.toString())));
        Assertions.assertEquals(0, memberRepository.findMemberByEmail(
                        registerMember.getEmail()).get().getTotalFines());

        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/card")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardTransactionRequest))
                .param("fine", fine1.getId().toString()))
                .andExpect(status().is(400));
        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(checkTransactionRequest))
                .param("fine", fine2.getId().toString()))
                .andExpect(status().is(400));
        mockMvc.perform(put(memberControllerPath +
                "/fines/transaction/cash")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cashTransactionRequest))
                .param("fine", fine3.getId().toString()))
                .andExpect(status().is(400));
    }
}
