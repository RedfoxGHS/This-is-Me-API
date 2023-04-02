package br.inatel.thisismeapi.exceptions;

public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException(String message) {
        super(message);
    }
}
