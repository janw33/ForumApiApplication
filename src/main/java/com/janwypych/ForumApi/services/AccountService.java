package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.dtos.AuthResponse;
import com.janwypych.ForumApi.dtos.CreateAccountRequest;
import com.janwypych.ForumApi.dtos.LoginRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.AccountAlreadyExistsException;
import com.janwypych.ForumApi.exceptions.InvalidCredentialsException;
import com.janwypych.ForumApi.mappers.AccountMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import jakarta.validation.Valid;
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
            throw new AccountAlreadyExistsException("Username is already taken");

        if(accountRepository.existsByEmail(account.getEmail()))
            throw new AccountAlreadyExistsException("Email is already in use");

        account.setCreatedAt(LocalDateTime.now());
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        Account savedAccount = accountRepository.save(account);
        String token = jwtService.generateToken(savedAccount);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Account account = accountRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid credentials"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword()))
            throw new InvalidCredentialsException("Invalid credentials");

        String token = jwtService.generateToken(account);

        return new AuthResponse(token);
    }
}
