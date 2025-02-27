package com.sau.swe.service.serviceImpl;

import com.sau.swe.dto.AccountResponse;
import com.sau.swe.dto.BalanceRequest;
import com.sau.swe.dto.CreateAccountDTO;
import com.sau.swe.dto.TransferRequest;
import com.sau.swe.entity.Account;
import com.sau.swe.repository.AccountRepository;
import com.sau.swe.repository.UserRepository;
import com.sau.swe.service.AccountService;
import com.sau.swe.utils.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

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
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance((long) (account.getBalance()+balanceRequest.getAmount()));
        accountRepository.save(account);
    }

    @Override
    public AccountResponse getAccountByUserId(Long userId) {
        Account account=accountRepository.findByUserId_Id(userId).orElseThrow();
        AccountResponse accountResponse=new AccountResponse();
        accountResponse.setAccountType(String.valueOf(account.getAccountType()));
        accountResponse.setBalance(account.getBalance());
        accountResponse.setTransferCode(getTransferCode());
        return accountResponse;
    }


    @Override
    public void moneyTransfer(TransferRequest request) {
        Account senderAccount = accountRepository.findByUserId_Id(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender Account not found"));

        if (senderAccount.getBalance()<request.getMoney()){
            throw new RuntimeException("insufficient funds");
        }
        Account recipientAccount =
                accountRepository.findByTransferCode(request.getCode())
                        .orElseThrow(() -> new RuntimeException("Recipient Account not found"));

        senderAccount.setBalance(senderAccount.getBalance()-request.getMoney());
        recipientAccount.setBalance(recipientAccount.getBalance()+request.getMoney());

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);



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
