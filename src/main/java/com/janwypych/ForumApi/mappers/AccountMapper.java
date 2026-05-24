package com.janwypych.ForumApi.mappers;

import com.janwypych.ForumApi.dtos.CreateAccountRequest;
import com.janwypych.ForumApi.entities.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    private final ModelMapper modelMapper;

    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Account mapFromCreateAccountRequest(CreateAccountRequest createAccountRequest) {
        return modelMapper.map(createAccountRequest, Account.class);
    }
}
