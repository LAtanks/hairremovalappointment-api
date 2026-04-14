package br.com.latanks.cidasdepilacao_api.exceptions.impl;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message){
        super(message);
    }
}
