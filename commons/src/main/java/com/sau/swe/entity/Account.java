package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
    private BigDecimal balance;

    @Column(name = "account_type")
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType = AccountType.TRY;

    @Column(name = "transfer_code")
    private String transferCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;

    @OneToMany(mappedBy = "account")
    private List<AccountCurrency> currencies;

    public enum AccountType{
        TRY,
        USD,
        EUR
    }
}
