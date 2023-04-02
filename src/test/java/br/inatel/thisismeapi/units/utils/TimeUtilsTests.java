package br.inatel.thisismeapi.units.utils;

import br.inatel.thisismeapi.utils.TimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TimeUtils.class)
public class TimeUtilsTests {

    @Test
    void testGetTimeInTextFormatWithTimeInMinGreaterThanOneHour() {

        Long timeInMin = 90L;
        String expected = "1h30m";
        String timeText = TimeUtils.getTimeInTextFormat(timeInMin);

        assertEquals(expected, timeText);
    }

    @Test
    void testGetTimeInTextFormatWithTimeInMinLessThanOneHour() {

        Long timeInMin = 35L;
        String expected = "35min";
        String timeText = TimeUtils.getTimeInTextFormat(timeInMin);

        assertEquals(expected, timeText);
    }
}
