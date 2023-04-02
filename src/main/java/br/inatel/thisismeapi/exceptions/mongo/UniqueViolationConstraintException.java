package br.inatel.thisismeapi.exceptions.mongo;

public class UniqueViolationConstraintException extends RuntimeException {

    public UniqueViolationConstraintException(String message) {
        super(message);
    }
}
