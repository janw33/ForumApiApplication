package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.AuthResponse;
import com.janwypych.ForumApi.dtos.CreateAccountRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.mappers.AccountMapper;
import com.janwypych.ForumApi.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final AccountMapper accountMapper;
    private final AccountService accountService;

    public AccountController(AccountMapper accountMapper, AccountService accountService) {
        this.accountMapper = accountMapper;
        this.accountService = accountService;
    }

    @PostMapping(path = "/accounts/create")
    public ResponseEntity<AuthResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest createAccountRequest
            ) {
        Account account = accountMapper.mapFromCreateAccountRequest(createAccountRequest);
        String token = accountService.createAccount(account);
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(authResponse);
    }
}
