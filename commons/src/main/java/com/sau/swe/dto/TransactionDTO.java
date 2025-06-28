package com.sau.swe.dto;

import com.sau.swe.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO {
    private String description;
    private BigDecimal amount;
    private String paymentType;
    private String category;
    private LocalDateTime date;
    private Boolean isIncome;

    public TransactionDTO(String description, BigDecimal amount, Transaction.PaymentType paymentType, String category, LocalDateTime date, Boolean isIncome) {
        this.description = description;
        this.amount = amount;
        this.paymentType = paymentType.name();
        this.category = category;
        this.date = date;
        this.isIncome = isIncome;
    }
}
