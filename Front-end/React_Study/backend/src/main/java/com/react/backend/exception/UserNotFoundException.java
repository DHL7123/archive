package com.react.backend.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException (Long id){
        super("Could not find user with id " + id);
    }
}
