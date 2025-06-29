package com.sau.swe.api;

import com.sau.swe.dto.ExpenseDTO;
import com.sau.swe.service.ExpenseService;
import com.sau.swe.utils.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpensesController {

    private final ExpenseService expenseService;

    @GetMapping("/get-expenses")
    public ResponseEntity<GenericResponse<List<ExpenseDTO>>> getExpense(@RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(GenericResponse.success(expenseService.getExpenses(userId)));
    }

    @PostMapping("/add-expense/{userId}")
    public ResponseEntity<GenericResponse<Void>> addExpense(@PathVariable(name = "userId") Long userId,
                                                             @RequestBody ExpenseDTO expense) {
        expenseService.addExpense(userId, expense);
        return ResponseEntity.ok(GenericResponse.success("expense.add.success"));
    }

    @DeleteMapping("/delete-expense/{expenseId}")
    public ResponseEntity<GenericResponse<Void>> deleteExpense(@PathVariable(name = "expenseId") Long expenseId) {
        expenseService.deleteById(expenseId);
        return ResponseEntity.ok(GenericResponse.success("expense.delete.success"));
    }

    @PutMapping("/update-expense/{expenseId}")
    public ResponseEntity<GenericResponse<Void>> updateExpense(@PathVariable(name = "expenseId") Long expenseId,
                                                               @RequestBody ExpenseDTO expense) {
        expenseService.update(expenseId,expense);
        return ResponseEntity.ok(GenericResponse.success("espense.update.success"));
    }

}
