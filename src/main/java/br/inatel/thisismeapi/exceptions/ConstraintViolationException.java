package br.inatel.thisismeapi.exceptions;

public class ConstraintViolationException extends RuntimeException {

    public ConstraintViolationException(String message) {
        super(message);
    }
}
