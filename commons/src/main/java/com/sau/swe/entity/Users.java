package com.sau.swe.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq_generator")
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "image")
    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "userId")
    private Set<Account> accounts;

    @OneToMany(mappedBy = "user")
    private List<FinancialGoals> goals;

    @ManyToMany(cascade = CascadeType.ALL
            , fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE"
            , joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)}
            , inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @JsonIgnore
    private List<Roles> roles;

    enum AccountStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }

    public String getUserCredential() {
        return firstName + " " + lastName;
    }
}
