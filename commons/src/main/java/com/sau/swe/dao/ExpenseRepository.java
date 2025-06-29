package com.sau.swe.dao;

import com.sau.swe.dto.ExpenseDTO;
import com.sau.swe.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    @Query(value = "select new com.sau.swe.dto.ExpenseDTO(" +
            "e.id, e.name, e.description, e.amount, e.category, e.lastPaymentDate, e.isRecurring,e.isPaid) " +
            "from Expense e " +
            "inner join e.user u " +
            "where u.id =:userId")
    List<ExpenseDTO> findAllByUserId(@Param("userId") Long userId);
}
