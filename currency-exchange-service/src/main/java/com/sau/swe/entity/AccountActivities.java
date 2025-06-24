package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ACCOUNT_ACTIVITIES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountActivities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "acc_activity_seq_generator")
    @SequenceGenerator(name = "acc_activity_seq_generator", sequenceName = "acc_activity_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "is_income")
    private Boolean isIncome;

}

