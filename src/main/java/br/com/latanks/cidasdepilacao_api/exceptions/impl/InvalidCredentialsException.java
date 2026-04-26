package br.com.latanks.cidasdepilacao_api.exceptions.impl;

import br.com.latanks.cidasdepilacao_api.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException(String message){
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
