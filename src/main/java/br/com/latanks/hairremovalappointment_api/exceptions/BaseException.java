package br.com.latanks.hairremovalappointment_api.exceptions;

public class BaseException extends RuntimeException{
    private final int status;

    public BaseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() { return status; }
}
