package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "TRANSACTIONS")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "transaction_seq_generator")
    @SequenceGenerator(name = "transaction_seq_generator", sequenceName = "transactions_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "category")
    private String category;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @OneToMany(fetch = FetchType.EAGER)
    private List<AccountActivities> accountActivities;

    public enum PaymentType {
        CASH,
        CREDIT_CARD,
        EXCHANGE
    }
}

