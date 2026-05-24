package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.AccountAlreadyExistsException;
import com.janwypych.ForumApi.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String createAccount(Account account) {
        if(accountRepository.existsByEmail(account.getEmail()))
            throw new AccountAlreadyExistsException("account already exists");

        account.setCreatedAt(LocalDateTime.now());

        accountRepository.save(account);

        String token = "temporary";

        return token;
    }
}
