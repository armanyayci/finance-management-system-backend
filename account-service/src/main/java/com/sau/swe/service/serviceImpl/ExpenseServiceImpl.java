package com.sau.swe.service.serviceImpl;

import com.sau.swe.dao.ExpenseRepository;
import com.sau.swe.dao.UserRepository;
import com.sau.swe.dto.ExpenseDTO;
import com.sau.swe.entity.Expense;
import com.sau.swe.entity.Users;
import com.sau.swe.service.ExpenseService;
import com.sau.swe.utils.exception.GenericFinanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public void addExpense(String username, ExpenseDTO expense) {
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new GenericFinanceException("generic.auth.userNotFound")
        );

        expenseRepository.save(Expense.builder()
                .name(expense.getName())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .lastPaymentDate(expense.getLastPaymentDate())
                .isRecurring(expense.getIsRecurring())
                .isPaid(expense.getIsPaid())
                .user(user)
                .build());
        log.info("User expenses saved. UserId: " + username);
    }

    @Override
    public List<ExpenseDTO> getExpenses(String username) {
        return expenseRepository.findAllByUsername(username);
    }

    @Override
    @Transactional
    public void deleteById(Long expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    @Override
    @Transactional
    public void update(Long expenseId, ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                () -> new GenericFinanceException("expense.not.found")
        );
        expense.setName(expenseDTO.getName());
        expense.setDescription(expenseDTO.getDescription());
        expense.setCategory(expenseDTO.getCategory());
        expense.setAmount(expenseDTO.getAmount());
        expense.setLastPaymentDate(expenseDTO.getLastPaymentDate());
        expense.setIsRecurring(expenseDTO.getIsRecurring());
        expense.setIsPaid(expenseDTO.getIsPaid());
        expenseRepository.save(expense);
        log.info("User expenses updated. ExpenseId " + expenseId);
    }
}
