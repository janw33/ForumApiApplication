package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.dtos.AuthResponse;
import com.janwypych.ForumApi.dtos.CreateAccountRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.AccountAlreadyExistsException;
import com.janwypych.ForumApi.mappers.AccountMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountMapper accountMapper, JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
        this.jwtService = jwtService;
    }

    public AuthResponse register(CreateAccountRequest createAccountRequest) {
        Account account = accountMapper.mapFromCreateAccountRequest(createAccountRequest);

        if(accountRepository.existsByUsername(account.getUsername()))
            throw new AccountAlreadyExistsException("account already exists");

        if(accountRepository.existsByEmail(account.getEmail()))
            throw new AccountAlreadyExistsException("account already exists");

        account.setCreatedAt(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        Account savedAccount = accountRepository.save(account);
        String token = jwtService.generateToken(savedAccount);

        return new AuthResponse(token);
    }
}
