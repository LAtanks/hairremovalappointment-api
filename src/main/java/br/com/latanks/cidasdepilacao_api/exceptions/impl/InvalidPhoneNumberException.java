package br.com.latanks.cidasdepilacao_api.exceptions.impl;

import br.com.latanks.cidasdepilacao_api.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidPhoneNumberException extends BaseException {
    public InvalidPhoneNumberException(String message){
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
