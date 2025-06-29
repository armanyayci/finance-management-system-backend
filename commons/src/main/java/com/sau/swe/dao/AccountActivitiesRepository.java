package com.sau.swe.dao;

import com.sau.swe.dto.TransactionDTO;
import com.sau.swe.entity.Account;
import com.sau.swe.entity.AccountActivities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountActivitiesRepository extends JpaRepository<AccountActivities,Long> {

    @Query(value = "select new com.sau.swe.dto.TransactionDTO(" +
            "aa.description, t.amount, t.paymentType, t.category, t.transactionTime, aa.isIncome) " +
            "from AccountActivities aa " +
            "inner join aa.transaction t " +
            "inner join aa.account acc " +
            "inner join acc.userId user " +
            "where user.username =:username and acc.accountType =:accountType and t.transactionTime >= :startDate " +
            "order by t.transactionTime desc")
    List<TransactionDTO> getTransactionListByAccountId(@Param("username") String username,
                                                       @Param("accountType")Account.AccountType accountType,
                                                       @Param("startDate") LocalDateTime startDate);


    @Query(value = "select new com.sau.swe.dto.TransactionDTO(" +
            "aa.description, t.amount, t.paymentType, t.category, t.transactionTime, aa.isIncome) " +
            "from AccountActivities aa " +
            "inner join aa.transaction t " +
            "inner join aa.account acc " +
            "inner join acc.userId user " +
            "where user.username =:username and t.transactionTime >= :startDate and t.transactionTime <= :endDate")
    List<TransactionDTO> getTransactionListByAccountIdForSpecificDateRange(@Param("username") String username, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
