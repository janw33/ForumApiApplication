package com.janwypych.ForumApi.exceptions;

public class AccountHasNoPermissionException extends RuntimeException{
    public AccountHasNoPermissionException(String message) {super(message);}
}
