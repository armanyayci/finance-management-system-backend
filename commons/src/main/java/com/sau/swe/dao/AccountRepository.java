package com.sau.swe.dao;

import com.sau.swe.dto.AccountResponse;
import com.sau.swe.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByTransferCode(String transferCode);
    Optional<Account> findByTransferCode(String transferCode);
    Optional<Account> findByUserId_Id(Long userId);

    @Query(value = "select new com.sau.swe.dto.AccountResponse(a.balance, CAST(a.accountType AS integer), a.transferCode )" +
            " FROM Account a" +
            " LEFT JOIN a.userId au" +
            " WHERE au.username = :username ")
    AccountResponse getAccountInfo(@Param("username") String username);


    @Query("select t from Account t " +
            "inner join Users u on u.id = t.userId.id " +
            "where u.id =:userId ")
    Optional<Account> getAccountByUserId(@Param("userId") Long userId);
}
