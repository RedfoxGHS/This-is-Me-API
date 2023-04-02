package br.inatel.thisismeapi.units.enums;

import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.WeekFields;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

public class DayOfWeekCustomTest {

    @Test
    void testGetDayOfWeekName() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        String expectedDayOfWeekName = "MONDAY";
        String actualDayOfWeekName = dayOfWeekCustom.getDayOfWeekName();
        assertEquals(expectedDayOfWeekName, actualDayOfWeekName);
    }

    @Test
    void testGetDayOfWeekNameWhenDayOfWeekIsTUESDAY() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.TUESDAY;
        String expectedDayOfWeekName = "TUESDAY";
        String actualDayOfWeekName = dayOfWeekCustom.getDayOfWeekName();
        assertEquals(expectedDayOfWeekName, actualDayOfWeekName);
    }

    @Test
    void testGetDayOfWeekNameWhenDayOfWeekIsWEDNESDAY() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.WEDNESDAY;
        String expectedDayOfWeekName = "WEDNESDAY";
        String actualDayOfWeekName = dayOfWeekCustom.getDayOfWeekName();
        assertEquals(expectedDayOfWeekName, actualDayOfWeekName);
    }

    @Test
    void testGetDayOfWeekNameWhenDayOfWeekIsTHURSDAY() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.THURSDAY;
        String expectedDayOfWeekName = "THURSDAY";
        String actualDayOfWeekName = dayOfWeekCustom.getDayOfWeekName();
        assertEquals(expectedDayOfWeekName, actualDayOfWeekName);
    }

    @Test
    void testGetDayOfWeekNameWhenDayOfWeekIsFRIDAY() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.FRIDAY;
        String expectedDayOfWeekName = "FRIDAY";
        String actualDayOfWeekName = dayOfWeekCustom.getDayOfWeekName();
        assertEquals(expectedDayOfWeekName, actualDayOfWeekName);
    }

    @Test
    void testGetDayOfWeekNameWhenDayOfWeekIsSATURDAY() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.SATURDAY;
        String expectedDayOfWeekName = "SATURDAY";
        String actualDayOfWeekName = dayOfWeekCustom.getDayOfWeekName();
        assertEquals(expectedDayOfWeekName, actualDayOfWeekName);
    }

    @Test
    void testGetDayOfWeekNameWhenDayOfWeekIsSUNDAY() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.SUNDAY;
        String expectedDayOfWeekName = "SUNDAY";
        String actualDayOfWeekName = dayOfWeekCustom.getDayOfWeekName();
        assertEquals(expectedDayOfWeekName, actualDayOfWeekName);
    }


    @Test
    void testOf() {
        DayOfWeekCustom expectedDayOfWeekCustom = DayOfWeekCustom.SUNDAY;
        DayOfWeekCustom actualDayOfWeekCustom = DayOfWeekCustom.of(1);
        assertEquals(expectedDayOfWeekCustom, actualDayOfWeekCustom);
    }

    @Test
    void testFromLocalDate() {
        LocalDate localDate = LocalDate.of(2021, 8, 2);
        DayOfWeekCustom expectedDayOfWeekCustom = DayOfWeekCustom.MONDAY;
        DayOfWeekCustom actualDayOfWeekCustom = DayOfWeekCustom.from(localDate);
        assertEquals(expectedDayOfWeekCustom, actualDayOfWeekCustom);
    }

    @Test
    void testFromLocalDateWhenLocalDateIsTuesday() {
        LocalDate localDate = LocalDate.of(2021, 8, 3);
        DayOfWeekCustom expectedDayOfWeekCustom = DayOfWeekCustom.TUESDAY;
        DayOfWeekCustom actualDayOfWeekCustom = DayOfWeekCustom.from(localDate);
        assertEquals(expectedDayOfWeekCustom, actualDayOfWeekCustom);
    }

    @Test
    void testFromDayOfWeekCustomWhenDayOfWeekCustomIsWednesday() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.WEDNESDAY;
        DayOfWeekCustom expectedDayOfWeek = DayOfWeekCustom.WEDNESDAY;
        DayOfWeekCustom actualDayOfWeek = DayOfWeekCustom.from(dayOfWeekCustom);
        assertEquals(expectedDayOfWeek, actualDayOfWeek);
    }

    @Test
    void testFromDateTimeExceptionWhenDayOfWeekIsInvalid() {
        try (MockedStatic<DayOfWeekCustom> dayOfWeekCustomMockedStatic = mockStatic(DayOfWeekCustom.class)) {
            dayOfWeekCustomMockedStatic.when(() -> DayOfWeekCustom.of(anyInt())).thenThrow(new DateTimeException("Invalid value for DayOfWeekCustom: 5"));
            dayOfWeekCustomMockedStatic.when(() -> DayOfWeekCustom.from(DayOfWeek.of(5))).thenCallRealMethod();
            assertThrows(DateTimeException.class, () -> DayOfWeekCustom.from(DayOfWeek.of(5)));
        }
    }

    @Test
    void testOfDateTimeExceptionWhenDayOfWeekIsInvalid() {
        int invalidDayOfWeek = 8;
        assertThrows(DateTimeException.class, () -> DayOfWeekCustom.of(invalidDayOfWeek));
    }

    @Test
    void testOfDateTimeExceptionWhenDayOfWeekIsInvalid2() {
        int invalidDayOfWeek = 0;
        assertThrows(DateTimeException.class, () -> DayOfWeekCustom.of(invalidDayOfWeek));
    }

    @Test
    void testIsSupportedWhenDayOfWeekIsSupported() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        assertTrue(dayOfWeekCustom.isSupported(ChronoField.DAY_OF_WEEK));
    }

    @Test
    void testIsSupportedWhenDayOfWeekIsNotSupported() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        assertFalse(dayOfWeekCustom.isSupported(ChronoField.DAY_OF_MONTH));
    }

    @Test
    void testIsSupportedWhenFieldIsNull() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        assertFalse(dayOfWeekCustom.isSupported(null));
    }

    @Test
    void testIsSupportedWhenFieldIsSupporteBy() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        assertTrue(dayOfWeekCustom.isSupported(WeekFields.ISO.dayOfWeek()));
    }

    @Test
    void testIsSupportedWhenFieldIsNotSupporteBy() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        assertFalse(dayOfWeekCustom.isSupported(WeekFields.ISO.weekOfMonth()));
    }

    @Test
    void testGetLongWhenDayOfWeekIsSupported() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        long expectedDayOfWeek = 2;
        long actualDayOfWeek = dayOfWeekCustom.getLong(ChronoField.DAY_OF_WEEK);
        assertEquals(expectedDayOfWeek, actualDayOfWeek);
    }

    @Test
    void testGetLongWhenDayOfWeekIsNotSupported() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        assertThrows(UnsupportedTemporalTypeException.class, () -> dayOfWeekCustom.getLong(ChronoField.DAY_OF_MONTH));
    }

    @Test
    void testGetLongWhenFieldIsNull() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        assertThrows(NullPointerException.class, () -> dayOfWeekCustom.getLong(null));
    }

    @Test
    void testGetLongWhenFieldIsSupportedBy() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        long expectedDayOfWeek = 2;
        long actualDayOfWeek = dayOfWeekCustom.getLong(WeekFields.ISO.dayOfWeek());
        assertEquals(expectedDayOfWeek, actualDayOfWeek);
    }

    @Test
    void testGetLongWhenFieldIsNotSupportedBy() {
        DayOfWeekCustom dayOfWeekCustom = DayOfWeekCustom.MONDAY;
        assertThrows(UnsupportedTemporalTypeException.class, () -> dayOfWeekCustom.getLong(WeekFields.ISO.weekOfMonth()));
    }
}
