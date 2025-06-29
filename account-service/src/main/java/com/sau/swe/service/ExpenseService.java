package com.sau.swe.service;

import com.sau.swe.dto.ExpenseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpenseService {
    void addExpense(Long userId, ExpenseDTO expenses);

    List<ExpenseDTO> getExpenses(Long userId);

    void deleteById(Long userId);

    void update(Long expenseId, ExpenseDTO expense);
}
