package br.inatel.thisismeapi.utils;

import br.inatel.thisismeapi.exceptions.ValidationsParametersException;

import java.util.Random;

public class RandomUtils {

    public static Long randomGenerate(Long startIn, Long endIn) {

        if (startIn == null || endIn == null)
            throw new ValidationsParametersException("StartIn and endIn not be null");

        Random random = new Random();
        return (long) (random.nextLong(endIn) + startIn);
    }
}
