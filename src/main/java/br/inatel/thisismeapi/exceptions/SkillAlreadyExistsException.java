package br.inatel.thisismeapi.exceptions;

public class SkillAlreadyExistsException extends RuntimeException {
    public SkillAlreadyExistsException(String message) {
        super(message);
    }
}
