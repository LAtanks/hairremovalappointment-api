package br.com.latanks.hairremovalappointment_api.exceptions.impl;

import br.com.latanks.hairremovalappointment_api.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidPhoneNumberException extends BaseException {
    public InvalidPhoneNumberException(String message){
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
