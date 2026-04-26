package br.com.latanks.cidasdepilacao_api.exceptions.impl;

import br.com.latanks.cidasdepilacao_api.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidDateTimeException extends BaseException {
    public InvalidDateTimeException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
