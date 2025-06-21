package com.sau.swe.dto;

import com.sau.swe.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO {
    private String description;
    private Long amount;
    private String paymentType;
    private String category;
    private LocalDateTime date;
    private Boolean isIncome;

    public TransactionDTO(String description, Long amount, Transaction.PaymentType paymentType, String category, LocalDateTime date, Boolean isIncome) {
        this.description = description;
        this.amount = amount;
        this.paymentType = paymentType.name();
        this.category = category;
        this.date = date;
        this.isIncome = isIncome;
    }
}
