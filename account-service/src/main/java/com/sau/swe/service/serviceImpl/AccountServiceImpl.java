package com.sau.swe.service.serviceImpl;

import com.sau.swe.dto.*;
import com.sau.swe.entity.Account;
import com.sau.swe.entity.AccountActivities;
import com.sau.swe.entity.Transaction;
import com.sau.swe.repository.AccountActivitiesRepository;
import com.sau.swe.repository.AccountRepository;
import com.sau.swe.repository.TransactionRepository;
import com.sau.swe.repository.UserRepository;
import com.sau.swe.service.AccountService;
import com.sau.swe.utils.Constants;
import com.sau.swe.utils.exception.GenericFinanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountActivitiesRepository accountActivitiesRepository;
    private final TransactionRepository transactionRepository;
    @Override
    public void createAccount(CreateAccountDTO dto)
    {
        accountRepository.save(Account.builder()
                .userId(userRepository.getReferenceById(dto.getUserId()))
                .accountType(Account.AccountType.valueOf(dto.getAccountType()))
                .balance(Constants.INITIAL_BALANCE)
                .transferCode(getTransferCode())
                .build());
    }
    @Override
    public void addBalance(BalanceRequest balanceRequest) {
        Account account=accountRepository.findById(balanceRequest.getAccountId())
                .orElseThrow(() -> new GenericFinanceException("account.notfound"));
        account.setBalance((long) (account.getBalance()+balanceRequest.getAmount()));
        accountRepository.save(account);
    }

    @Override
    public AccountResponse getAccountByUsername(String username) {
        AccountResponse accountResponse = accountRepository.getAccountInfo(username);
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);
        List<TransactionDTO> accountTransactions = accountActivitiesRepository.getTransactionListByAccountId(username,startDate);
        accountResponse.setLastTransactions(accountTransactions);
        return accountResponse;
    }

    @Override
    public List<TransactionDTO> expenseAnalyze(String username,LocalDateTime startDate,LocalDateTime endDate) {
        return accountActivitiesRepository.getTransactionListByAccountIdForSpecificDateRange(username,startDate,endDate);
    }

    @Override
    @Transactional
    public void moneyTransfer(TransferRequest request) {
        Account senderAccount = accountRepository.findByUserId_Id(request.getSenderId())
                .orElseThrow(() -> new GenericFinanceException("account.sender.notfound"));

        if (senderAccount.getBalance()<request.getMoney()){
            throw new GenericFinanceException("account.insufficient.funds");
        }
        Account recipientAccount =
                accountRepository.findByTransferCode(request.getCode())
                        .orElseThrow(() -> new GenericFinanceException("account.transfercode.notfound"));

        senderAccount.setBalance(senderAccount.getBalance()-request.getMoney());
        recipientAccount.setBalance(recipientAccount.getBalance()+request.getMoney());

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        Transaction transaction = Transaction.builder()
                .amount(request.getMoney())
                .category("TRANSFER")
                .paymentType(Transaction.PaymentType.CASH)
                .transactionTime(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        AccountActivities senderActivity = AccountActivities.builder()
                .description("Money Transfer to " + recipientAccount.getUserId().getUserCredential() + " - Description: " + request.getDescription())
                .transaction(transaction)
                .account(senderAccount)
                .isIncome(false)
                .build();

        accountActivitiesRepository.save(senderActivity);
        AccountActivities recipientActivity= AccountActivities.builder()
                .description("Money transfer from " + senderAccount.getUserId().getUserCredential() + " - Description: " + request.getDescription())
                .transaction(transaction)
                .account(recipientAccount)
                .isIncome(true)
                .build();
        accountActivitiesRepository.save(recipientActivity);
        log.info("save transaction completed. SenderId: {}, RecipientId: {}", senderAccount.getUserId().getId(), recipientAccount.getUserId().getId());
    }


    private String getTransferCode(){
        String transferCode;
        do {
            transferCode = generateTransferCode();
        }
        while (accountRepository.existsByTransferCode(transferCode));
        return transferCode;
    }

    private String generateTransferCode() {
        Random rand = new Random();
        int LENGTH = Constants.TRANSFER_CODE_CHARSET.length();
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < Constants.TRANSFER_CODE_CHARSET.length(); i++) {
            sb.append(Constants.TRANSFER_CODE_CHARSET.charAt(rand.nextInt(LENGTH)));
        }
        return sb.toString();
    }


}
