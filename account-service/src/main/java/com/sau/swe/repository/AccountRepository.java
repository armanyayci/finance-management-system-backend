package com.sau.swe.repository;

import com.sau.swe.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByTransferCode(String transferCode);
    Optional<Account> findByTransferCode(String transferCode);
    Optional<Account> findByUserId_Id(Long userId);
}
