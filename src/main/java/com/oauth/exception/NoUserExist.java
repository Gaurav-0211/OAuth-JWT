package com.oauth.exception;

public class NoUserExist extends RuntimeException{
    private String message;
    public NoUserExist(){}

    public NoUserExist(String message){
        super();
        this.message = message;
    }
}
