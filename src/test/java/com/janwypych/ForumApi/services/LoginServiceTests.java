package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.security.JwtService;
import com.janwypych.ForumApi.dtos.AuthResponse;
import com.janwypych.ForumApi.dtos.LoginRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.InvalidCredentialsException;
import com.janwypych.ForumApi.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTests {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testThatLoginThrowsInvalidCredentialsExceptionWhenFindAccountByEmailReturnsEmpty() {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();

        when(accountRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> accountService.login(loginRequest)
        );

        verify(jwtService, never()).generateToken(any(Account.class));
    }

    @Test
    public void testThatLoginThrowsInvalidCredentialsExceptionWhenPasswordIsInvalid() {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        Account account = TestDataUtil.createAccount();

        when(accountRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(account));

        when(passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> accountService.login(loginRequest)
        );

        verify(jwtService, never()).generateToken(any(Account.class));
    }

    @Test
    public void testThatLoginReturnsTokenWhenCredentialsAreValid() {
        LoginRequest loginRequest = TestDataUtil.createLoginRequest();
        Account account = TestDataUtil.createAccount();

        when(accountRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(account));

        when(passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())).thenReturn(true);

        when(jwtService.generateToken(account)).thenReturn("jwt-token");

        AuthResponse response = accountService.login(loginRequest);

        assertEquals("jwt-token", response.getToken());

        verify(jwtService).generateToken(account);
        verify(passwordEncoder).matches(loginRequest.getPassword(), account.getPassword());
    }
}
