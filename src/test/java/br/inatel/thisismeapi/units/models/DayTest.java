package br.inatel.thisismeapi.units.models;

import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import br.inatel.thisismeapi.models.Day;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Day.class})
class DayTest {

    @Test
    void testCreateNewInstance() {
        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        day.setStartTime("10:00");
        day.setEndTime("11:00");

        assertEquals(DayOfWeekCustom.FRIDAY, day.getDayOfWeekCustom());
        assertEquals("10:00", day.getStartTime());
        assertEquals("11:00", day.getEndTime());
    }

    @Test
    void testGetInterval() {
        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        day.setStartTime("10:00");
        day.setEndTime("11:00");

        assertEquals(60, day.getIntervalInMin());
    }

    @Test
    void testGetIntervalWithStartTimeNull() {
        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        day.setEndTime("11:00");

        assertNull(day.getIntervalInMin());
    }

    @Test
    void testGetIntervalWithEndTimeNull() {
        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        day.setStartTime("10:00");

        assertNull(day.getIntervalInMin());
    }

    @Test
    void testCompareToWithThisObjectGreaterThanAnotherObject() {

        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.SATURDAY);
        Day day2 = new Day();
        day2.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);

        int result = day.compareTo(day2);

        assertEquals(1, result);
    }

    @Test
    void testCompareToWithThisObjectLessThanAnotherObject() {

        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.THURSDAY);
        Day day2 = new Day();
        day2.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);

        int result = day.compareTo(day2);

        assertEquals(-1, result);
    }

    @Test
    void testCompareToWithThisObjectEqualsAnotherObject() {

        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        Day day2 = new Day();
        day2.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);

        int result = day.compareTo(day2);

        assertEquals(0, result);
    }

    @Test
    void testEqualsSameObject() {
        Day day = new Day();
        Boolean result = day.equals(day);

        assertTrue(result);
    }

    @Test
    void testEqualsSameObjectWithDifferentAddress() {
        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        Day day2 = new Day();
        day2.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        Boolean result = day.equals(day2);

        assertTrue(result);
    }

    @Test
    void testEqualsObjectNull() {
        Day day = new Day();
        Boolean result = day.equals(null);

        assertFalse(result);
    }

    @Test
    void testEqualsObjectDifferent() {
        Day day = new Day();
        Boolean result = day.equals(new Quest());

        assertFalse(result);
    }

    @Test
    void testEqualsSameObjectButValueDifferent() {
        Day day = new Day();
        day.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        Day day2 = new Day();
        day2.setDayOfWeekCustom(DayOfWeekCustom.MONDAY);
        Boolean result = day.equals(day2);

        assertFalse(result);
    }
}
