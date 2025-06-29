package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EXPENSE")
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_seq_generator")
    @SequenceGenerator(name = "expense_seq_generator", sequenceName = "expense_sequence", allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "category")
    private String category;
    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;
    @Column(name = "is_recurring")
    private Boolean isRecurring;
    @Column(name = "is_paid")
    private Boolean isPaid;
    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;
}
