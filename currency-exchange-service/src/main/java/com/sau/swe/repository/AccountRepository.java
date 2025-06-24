package com.sau.swe.repository;

import com.sau.swe.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select t from Account t " +
            "inner join Users u on u.id = t.userId.id " +
            "where u.id =:userId ")
    Optional<Account> getAccountByUserId(@Param("userId") Long userId);

}
