package com.oauth.exception;

public class UserAlreadyExist extends RuntimeException{
    private String message;

    public UserAlreadyExist(){}

    public UserAlreadyExist(String message){
        super();
        this.message = message;
    }
}
