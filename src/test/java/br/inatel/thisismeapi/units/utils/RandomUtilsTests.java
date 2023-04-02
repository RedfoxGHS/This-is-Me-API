package br.inatel.thisismeapi.units.utils;

import br.inatel.thisismeapi.exceptions.ValidationsParametersException;
import br.inatel.thisismeapi.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = RandomUtils.class)
class RandomUtilsTests {


    @Test
    void testRandomGenerateSuccess() {

        Long number = RandomUtils.randomGenerate(0L, 10L);

        assertNotNull(number);
        assertNotEquals(-1, number);
        assertNotEquals(11, number);
    }

    @Test
    void testRandomGenerateThrowExceptionWhenStartInIsNull() {

        Long number = RandomUtils.randomGenerate(0L, 10L);

        ValidationsParametersException exception = assertThrows(ValidationsParametersException.class, () -> {
            RandomUtils.randomGenerate(null, 100L);
        });

        assertEquals("StartIn and endIn not be null", exception.getMessage());
    }

    @Test
    void testRandomGenerateThrowExceptionWhenEndInIsNull() {

        Long number = RandomUtils.randomGenerate(0L, 10L);

        ValidationsParametersException exception = assertThrows(ValidationsParametersException.class, () -> {
            RandomUtils.randomGenerate(1L, null);
        });

        assertEquals("StartIn and endIn not be null", exception.getMessage());
    }

}
