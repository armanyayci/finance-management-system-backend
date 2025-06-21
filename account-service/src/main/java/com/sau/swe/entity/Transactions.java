package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTIONS")
@Data
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="amount")
    private Double amount;

    @Column(name = "category")
    private String category;

    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @OneToOne(mappedBy = "account_activities")
    private AccountActivities accountActivities;

}
