package br.inatel.thisismeapi.exceptions;

public class AlreadyDoneException extends RuntimeException {
    public AlreadyDoneException(String message) {
        super(message);
    }
}
