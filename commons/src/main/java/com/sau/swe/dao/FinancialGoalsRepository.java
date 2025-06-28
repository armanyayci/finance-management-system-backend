package com.sau.swe.dao;

import com.sau.swe.entity.FinancialGoals;
import com.sau.swe.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialGoalsRepository extends JpaRepository<FinancialGoals, Long> {

    List<FinancialGoals> findFinancialGoalsByUserId(Long id);

    Long user(Users user);
}
