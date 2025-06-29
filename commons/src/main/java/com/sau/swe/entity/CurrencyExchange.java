package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CURRENCY_EXCHANGE")
@Builder
public class CurrencyExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "currency_seq_generator")
    @SequenceGenerator(name = "currency_seq_generator", sequenceName = "currency_exchange_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "from_currency" ,nullable = false)
    private String fromCurrency;

    @Column(name = "to_currency", nullable = false)
    private String toCurrency;

    @Column(name = "exchange_rate", nullable = false)
    private BigDecimal exchangeRate;

    @Column(name = "exchange_date", nullable = false)
    private LocalDateTime exchangeDate;

    @Column (name = "transaction_id",nullable = false)
    private Long transactionId;
}
