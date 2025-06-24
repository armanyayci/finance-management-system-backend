package com.sau.swe.repository;

import com.sau.swe.entity.AccountCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountCurrencyRepository extends JpaRepository<AccountCurrency, Long> {

    @Query("select ac from AccountCurrency ac " +
            "inner join ac.account a " +
            "inner join a.userId u " +
            "where ac.currencyName = :currencyName and u.id = :userId")
    Optional<AccountCurrency> getAccountCurrencyWithUserIdAndCurrencyName(@Param("currencyName") String currencyName, @Param("userId") Long userId);
}
