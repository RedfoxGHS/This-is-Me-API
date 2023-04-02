package br.inatel.thisismeapi.utils;

import br.inatel.thisismeapi.exceptions.ConstraintViolationException;

public final class UserUtils {

    public static boolean verifyPassword(String password, String verifyPassword) {

        if (password == null || verifyPassword == null)
            throw new ConstraintViolationException("Senha não pode ser nula!");

        if (password.isBlank() || verifyPassword.isBlank())
            throw new ConstraintViolationException("Senha não pode ser deixada em branco!");

        if (!password.equals(verifyPassword))
            throw new ConstraintViolationException("As senhas não coincidem!");

        return true;
    }

    public static Boolean verifyDecryptedPasswordLength(String password) {

        if (password.length() < 5 || password.length() > 30)
            throw new ConstraintViolationException("Senha deve conter no mínimo 5 e no máximo 30 dígitos!");

        return true;
    }

    public static Boolean verifyEmail(String email) {

        if (email == null)
            throw new ConstraintViolationException("Email não pode ser nulo!");

        if (email.isBlank())
            throw new ConstraintViolationException("Email não pode ser deixado em branco!");

        if (email.length() > 120)
            throw new ConstraintViolationException("Email não pode conter mais de 120 dígitos!");

        if (!email.matches(".+[@].+[\\\\.].+"))
            throw new ConstraintViolationException("Email inválido!");

        return true;
    }
}
