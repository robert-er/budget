package com.home.budget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date operationDate;
    private Date postingDate;
    private String description;
    private String fromAccount;
    private String toAccount;
    private String cardNumber;
    private String title;
    private BigDecimal amount;
    private BigDecimal balanceAfter;

    @Builder
    public History(Date operationDate,
                   Date postingDate,
                   String description,
                   String fromAccount,
                   String toAccount,
                   String cardNumber,
                   String title,
                   BigDecimal amount,
                   BigDecimal balanceAfter) {
        this.operationDate = operationDate;
        this.postingDate = postingDate;
        this.description = description;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.cardNumber = cardNumber;
        this.title = title;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(operationDate, history.operationDate)
                && Objects.equals(postingDate, history.postingDate)
                && Objects.equals(description, history.description)
                && Objects.equals(fromAccount, history.fromAccount)
                && Objects.equals(toAccount, history.toAccount)
                && Objects.equals(cardNumber, history.cardNumber)
                && Objects.equals(title, history.title)
                && Objects.equals(amount, history.amount)
                && Objects.equals(balanceAfter, history.balanceAfter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                operationDate,
                postingDate,
                description,
                fromAccount,
                toAccount,
                cardNumber,
                title,
                amount,
                balanceAfter);
    }
}
