package com.janwypych.ForumApi.controllers;

import com.janwypych.ForumApi.dtos.auth.AuthResponse;
import com.janwypych.ForumApi.dtos.auth.CreateAccountRequest;
import com.janwypych.ForumApi.dtos.auth.LoginRequest;
import com.janwypych.ForumApi.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/api/v1/auth/register")
    public ResponseEntity<AuthResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest createAccountRequest
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.register(createAccountRequest));
    }

    @PostMapping(path = "/api/v1/auth/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
            ) {
        return ResponseEntity.ok(accountService.login(loginRequest));
    }
}
