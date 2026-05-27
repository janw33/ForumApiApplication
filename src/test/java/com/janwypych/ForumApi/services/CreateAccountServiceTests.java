package com.janwypych.ForumApi.services;

import com.janwypych.ForumApi.TestDataUtil;
import com.janwypych.ForumApi.dtos.AuthResponse;
import com.janwypych.ForumApi.dtos.CreateAccountRequest;
import com.janwypych.ForumApi.entities.Account;
import com.janwypych.ForumApi.exceptions.AccountAlreadyExistsException;
import com.janwypych.ForumApi.mappers.AccountMapper;
import com.janwypych.ForumApi.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAccountServiceTests {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testThatRegisterThrowsAccountAlreadyExistsExceptionWhenUsernameIsUnavailable() {
        CreateAccountRequest request = new CreateAccountRequest();

        Account mappedAccount = Account.builder()
                .username("test")
                .email("test@test.com")
                .password("password123")
                .build();

        when(accountMapper.mapFromCreateAccountRequest(request))
                .thenReturn(mappedAccount);

        when(accountRepository.existsByUsername(mappedAccount.getUsername()))
                .thenReturn(true);

        assertThrows(
                AccountAlreadyExistsException.class,
                () -> accountService.register(request)
        );

        verify(accountRepository, never()).save(any(Account.class));
        verify(jwtService, never()).generateToken(any(Account.class));
    }

    @Test
    public void testThatRegisterThrowsAccountAlreadyExistsExceptionWhenEmailIsUnavailable() {
        CreateAccountRequest request = new CreateAccountRequest();

        Account mappedAccount = Account.builder()
                .username("test")
                .email("test@test.com")
                .password("password123")
                .build();

        when(accountMapper.mapFromCreateAccountRequest(request))
                .thenReturn(mappedAccount);

        when(accountRepository.existsByUsername(mappedAccount.getUsername()))
                .thenReturn(false);

        when(accountRepository.existsByEmail(mappedAccount.getEmail()))
                .thenReturn(true);

        assertThrows(
                AccountAlreadyExistsException.class,
                () -> accountService.register(request)
        );

        verify(accountRepository, never()).save(any(Account.class));
        verify(jwtService, never()).generateToken(any(Account.class));
    }

    @Test
    public void testThatRegisterReturnsTokenWhenEmailIsAvailable() {
        CreateAccountRequest request = new CreateAccountRequest();

        Account mappedAccount = Account.builder()
                .username("test")
                .email("test@test.com")
                .password("password123")
                .build();

        Account savedAccount = TestDataUtil.createAccount();

        when(accountMapper.mapFromCreateAccountRequest(request))
                .thenReturn(mappedAccount);

        when(accountRepository.existsByUsername(mappedAccount.getUsername()))
                .thenReturn(false);

        when(accountRepository.existsByEmail(mappedAccount.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(mappedAccount.getPassword()))
                .thenReturn("hashedPassword");

        when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);

        when(jwtService.generateToken(savedAccount))
                .thenReturn("jwt-token");

        AuthResponse response = accountService.register(request);

        assertEquals("jwt-token", response.getToken());

        verify(accountRepository).save(any(Account.class));

        verify(jwtService).generateToken(savedAccount);

        verify(passwordEncoder).encode("password123");
    }
}
