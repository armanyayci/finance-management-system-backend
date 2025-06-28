package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCOUNT_CURRENCY")
@Builder
public class AccountCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "currency_seq_generator")
    @SequenceGenerator(name = "currency_seq_generator", sequenceName = "currency_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
}
