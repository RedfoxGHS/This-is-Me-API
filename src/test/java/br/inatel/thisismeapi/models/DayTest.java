package br.inatel.thisismeapi.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ServletWebServerFactoryAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ActiveProfiles("dev")
class DayTest {

    @Test
    void testCreateNewInstance(){
        Day day = new Day();
        day.setDayOfWeek(DayOfWeek.FRIDAY);
        day.setStartTime("10:00");
        day.setEndTime("11:00");

        assertEquals(DayOfWeek.FRIDAY, day.getDayOfWeek());
        assertEquals("10:00", day.getStartTime());
        assertEquals("11:00", day.getEndTime());
    }

    @Test
    void testGetInterval(){
        Day day = new Day();
        day.setDayOfWeek(DayOfWeek.FRIDAY);
        day.setStartTime("10:00");
        day.setEndTime("11:00");

        assertEquals(60, day.getIntervalInMin());
    }

    @Test
    void testCalculateXp(){
        Day day = new Day();
        day.setDayOfWeek(DayOfWeek.FRIDAY);
        day.setStartTime("10:00");
        day.setEndTime("11:00");

        assertEquals(12, day.calculateXp());
    }
}
