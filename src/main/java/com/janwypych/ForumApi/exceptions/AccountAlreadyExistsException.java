package com.janwypych.ForumApi.exceptions;

public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException(String message){super(message);}
}
