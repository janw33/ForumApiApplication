package com.janwypych.ForumApi.exceptions;

public class UserNotAuthorException extends RuntimeException{
    public UserNotAuthorException(String message) {super(message);}
}
