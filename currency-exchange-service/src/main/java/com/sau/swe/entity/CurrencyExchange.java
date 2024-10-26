package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CURRENCY_EXCHANGE")
public class CurrencyExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_currency" ,nullable = false)
    private String fromCurrency;

    @Column(name = "to_currency", nullable = false)
    private String toCurrency;

    @Column(name = "exchange_rate", nullable = false)
    private BigDecimal exchangeRate;

    @Column(name = "exchange_date", nullable = false)
    private LocalDate exchangeDate;
}
