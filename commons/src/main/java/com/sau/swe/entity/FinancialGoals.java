package com.sau.swe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FINANCIAL_GOALS")
@Builder
public class FinancialGoals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "goal_seq_generator")
    @SequenceGenerator(name = "goal_seq_generator", sequenceName = "goal_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "account_type")
    @Enumerated(EnumType.ORDINAL)
    private Account.AccountType accountType = Account.AccountType.TRY;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

}
