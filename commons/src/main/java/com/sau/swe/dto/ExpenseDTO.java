package com.sau.swe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDate lastPaymentDate;
    private Boolean isRecurring;
    private Boolean isPaid;

    public ExpenseDTO(Long id, String name, String description, BigDecimal amount, String category, LocalDate lastPaymentDate, Boolean isRecurring, Boolean isPaid) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.lastPaymentDate = lastPaymentDate;
        this.isRecurring = isRecurring;
        this.isPaid = isPaid;
    }
}
