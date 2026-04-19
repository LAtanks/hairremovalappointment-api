package br.com.latanks.cidasdepilacao_api.exceptions.impl;

public class InvalidPhoneNumberException extends RuntimeException{
    public InvalidPhoneNumberException(String message){
        super(message);
    }
}
