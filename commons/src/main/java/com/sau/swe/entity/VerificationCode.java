package com.sau.swe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VERIFICATION_CODES")
@Builder
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @Column(name = "verification_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    public enum VerificationType {
        TWO_FACTOR_AUTH,
        PASSWORD_RESET,
        EMAIL_VERIFICATION
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
} 