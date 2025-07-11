package com.sau.swe.dao;

import com.sau.swe.dto.CurrencyListDTO;
import com.sau.swe.entity.AccountCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountCurrencyRepository extends JpaRepository<AccountCurrency,Long> {
    @Query("select ac from AccountCurrency ac " +
            "inner join ac.account a " +
            "inner join a.userId u " +
            "where ac.currencyName = :currencyName and u.id = :userId")
    Optional<AccountCurrency> getAccountCurrencyWithUserIdAndCurrencyName(@Param("currencyName") String currencyName, @Param("userId") Long userId);


    @Query(value = "select new com.sau.swe.dto.CurrencyListDTO(" +
            "ac.currencyName,ac.amount) " +
            "from AccountCurrency ac " +
            "inner join ac.account acc " +
            "inner join acc.userId u " +
            "where u.id =:userId")
    List<CurrencyListDTO> getUserCurrencies(@Param("userId") Long userId);
}
