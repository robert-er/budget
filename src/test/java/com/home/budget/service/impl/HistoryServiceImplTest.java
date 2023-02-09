package com.home.budget.service.impl;

import com.home.budget.model.History;
import com.home.budget.service.HistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HistoryServiceImplTest {

    @Autowired
    private HistoryService service;

    @Test
    public void shouldFindById() {
        //Given
        History givenHistory = History.builder()
                .operationDate(Date.valueOf("1988-02-19"))
                .postingDate(Date.valueOf("1988-02-22"))
                .description("PRL")
                .fromAccount("22113344")
                .toAccount("88777666555")
                .title("Wynagrodzenie")
                .amount(new BigDecimal("1000.00"))
                .balanceAfter(new BigDecimal("500.00"))
                .build();
        History savedHistory = service.add(givenHistory);
        Long id = savedHistory.getId();

        //When
        History retrievedHistory = service.findById(id).get();

        //Then
        assertEquals(givenHistory.getOperationDate(), retrievedHistory.getOperationDate());
        assertEquals(givenHistory.getPostingDate(), retrievedHistory.getPostingDate());
        assertEquals(givenHistory.getDescription(), retrievedHistory.getDescription());
        assertEquals(givenHistory.getFromAccount(), retrievedHistory.getFromAccount());
        assertEquals(givenHistory.getToAccount(), retrievedHistory.getToAccount());
        assertEquals(givenHistory.getTitle(), retrievedHistory.getTitle());
        assertEquals(givenHistory.getAmount(), retrievedHistory.getAmount());
        assertEquals(givenHistory.getBalanceAfter(), retrievedHistory.getBalanceAfter());
    }

    @Test
    public void shouldGetAll() {
        //Given
        History givenHistory1 = History.builder()
                .operationDate(Date.valueOf("2022-12-12"))
                .postingDate(Date.valueOf("2022-12-14"))
                .description("Przelew testowy")
                .fromAccount("77 8888 2835 0000 0001 4238 9988")
                .toAccount("10 1001 2529 0000 0001 5189 2222")
                .title("Przelew 1")
                .amount(new BigDecimal("123.45"))
                .balanceAfter(new BigDecimal("845.87"))
                .build();
        History savedHistory1 = service.add(givenHistory1);

        History givenHistory2 = History.builder()
                .operationDate(Date.valueOf("2021-11-15"))
                .postingDate(Date.valueOf("2021-11-16"))
                .description("Przelew testowy 2")
                .fromAccount("66 8888 2835 0000 0001 4238 6666")
                .toAccount("66 6666 2529 0000 6666 5189 2222")
                .title("Przelew 2")
                .amount(new BigDecimal("222123.45"))
                .balanceAfter(new BigDecimal("222845.87"))
                .build();
        History savedHistory2 = service.add(givenHistory2);

        //When
        List<History> retrievedList = service.getAll();

        //Then
        assertTrue(retrievedList.stream()
                .filter(e -> e.getDescription().equals("Przelew testowy"))
                .anyMatch(e -> e.equals(savedHistory1)));
        assertTrue(retrievedList.stream()
                .filter(e -> e.getAmount().compareTo(new BigDecimal("222123.45")) == 0)
                .allMatch(e -> e.equals(savedHistory2)));
    }

    @Test
    public void shouldAdd() {
        //Given
        History givenHistory = History.builder()
                .operationDate(Date.valueOf("2023-01-26"))
                .postingDate(Date.valueOf("2023-01-26"))
                .description("UZNANIE")
                .fromAccount("57 1090 2835 0000 0001 4238 2303")
                .toAccount("12 1090 2529 0000 0001 5189 3244")
                .title("Promocja Godna Polecenia 1 23")
                .amount(new BigDecimal("25.63"))
                .balanceAfter(new BigDecimal("1718.44"))
                .build();

        //When
        History savedHistory = service.add(givenHistory);

        //Then
        Long id = savedHistory.getId();
        History retrievedHistory = service.findById(id).get();

        assertEquals(givenHistory.getOperationDate(), retrievedHistory.getOperationDate());
        assertEquals(givenHistory.getPostingDate(), retrievedHistory.getPostingDate());
        assertEquals(givenHistory.getDescription(), retrievedHistory.getDescription());
        assertEquals(givenHistory.getFromAccount(), retrievedHistory.getFromAccount());
        assertEquals(givenHistory.getToAccount(), retrievedHistory.getToAccount());
        assertEquals(givenHistory.getTitle(), retrievedHistory.getTitle());
        assertEquals(givenHistory.getAmount(), retrievedHistory.getAmount());
        assertEquals(givenHistory.getBalanceAfter(), retrievedHistory.getBalanceAfter());
    }
}