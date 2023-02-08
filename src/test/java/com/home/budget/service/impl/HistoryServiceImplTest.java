package com.home.budget.service.impl;

import com.home.budget.model.History;
import com.home.budget.service.HistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HistoryServiceImplTest {

    @Autowired
    private HistoryService service;

    @Test
    public void shouldFindById() {

    }

    @Test
    public void shouldGetAll() {
    }

    @Test
    public void shouldAdd() throws ParseException {
        //Given
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        History givenHistory = History.builder()
                .operationDate(formatter.parse("2023-01-26"))
                .postingDate(formatter.parse("2023-01-26"))
                .description("UZNANIE")
                .fromAccount("57 1090 2835 0000 0001 4238 2303")
                .toAccount(" 12 1090 2529 0000 0001 5189 3244")
                .title(" Promocja Godna Polecenia 1 23")
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