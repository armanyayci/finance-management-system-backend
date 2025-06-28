package com.sau.swe.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class GoalRequestDTO {

    private String description;
    private String accountType;
    private BigDecimal amount;
    private Long userId;

}
