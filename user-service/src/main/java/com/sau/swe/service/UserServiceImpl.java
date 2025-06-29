package com.sau.swe.service;

import com.sau.swe.dao.FinancialGoalsRepository;
import com.sau.swe.dao.UserRepository;
import com.sau.swe.dto.GoalRequestDTO;
import com.sau.swe.entity.Account;
import com.sau.swe.entity.FinancialGoals;
import com.sau.swe.entity.Users;
import com.sau.swe.exceptions.UserNotFoundException;
import com.sau.swe.interfaces.UserService;

import com.sau.swe.utils.exception.GenericFinanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FinancialGoalsRepository financialGoalsRepository;
    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void addImage(Long userId, MultipartFile file) {
        Users user = userRepository.findById(userId).orElseThrow(
                ()-> new GenericFinanceException("generic.auth.userNotFound"));
        try {
            user.setImage(file.getBytes());
        } catch (IOException e) {
            throw new GenericFinanceException("user.profile.add.image.fail");
        }
        userRepository.save(user);
        log.info("image saved successfully, userId: {}", userId);
    }

    @Override
    @Transactional
    public void addGoal(List<GoalRequestDTO> goals) {
        for (GoalRequestDTO dto : goals) {
            Users user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new GenericFinanceException("generic.auth.userNotFound"));
            FinancialGoals goal = FinancialGoals.builder()
                    .description(dto.getDescription())
                    .accountType(Account.AccountType.valueOf(dto.getAccountType()))
                    .amount(dto.getAmount())
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .build();
            financialGoalsRepository.save(goal);
            log.info("goal saved successfully, userId: {}", dto.getUserId());
        }
    }

    @Override
    public List<FinancialGoals> getFinancialGoals(Long userId) {
        return financialGoalsRepository.findFinancialGoalsByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteGoal(Long goalId) {
        financialGoalsRepository.deleteById(goalId);
    }
}

