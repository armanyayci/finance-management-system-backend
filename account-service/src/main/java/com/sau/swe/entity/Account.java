package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ACCOUNT")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "account_seq_generator")
    @SequenceGenerator(name = "account_seq_generator", sequenceName = "account_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "account_type")
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType = AccountType.TR;

    @Column(name = "transfer_code")
    private String transferCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;

    public enum AccountType{
        TR,
        USD,
        EURO,
        XAU
    }
}

