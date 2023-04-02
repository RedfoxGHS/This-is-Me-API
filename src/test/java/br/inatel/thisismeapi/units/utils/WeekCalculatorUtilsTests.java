package br.inatel.thisismeapi.units.utils;

import br.inatel.thisismeapi.utils.WeekCalculatorUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = WeekCalculatorUtils.class)
class WeekCalculatorUtilsTests {


    @Test
    void testVerifyDateBelongsToCurrentWeekMustBeTrue() {

        LocalDate today = LocalDate.of(2022, 11, 3);
        Integer numberDayOfWeekFromToday = DayOfWeek.from(today).getValue();
        LocalDate endDate = LocalDate.of(2022, 11, 5);

        try (MockedStatic<LocalDate> localDateMockedStatic = Mockito.mockStatic(LocalDate.class)) {
            localDateMockedStatic.when(LocalDate::now).thenReturn(today);
            localDateMockedStatic.when(() -> LocalDate.now().minusDays(numberDayOfWeekFromToday)).thenCallRealMethod();

            Boolean result = WeekCalculatorUtils.verifyDateBelongsToCurrentWeek(endDate);
            assertTrue(result);
        }
    }

    @Test
    void testVerifyDateBelongsToCurrentWeekMustBeFalse() {

        LocalDate today = LocalDate.of(2022, 11, 3);
        Integer numberDayOfWeekFromToday = DayOfWeek.from(today).getValue();
        LocalDate endDate = LocalDate.of(2022, 11, 6);

        try (MockedStatic<LocalDate> localDateMockedStatic = Mockito.mockStatic(LocalDate.class)) {
            localDateMockedStatic.when(LocalDate::now).thenReturn(today);
            localDateMockedStatic.when(() -> LocalDate.now().minusDays(numberDayOfWeekFromToday)).thenCallRealMethod();

            Boolean result = WeekCalculatorUtils.verifyDateBelongsToCurrentWeek(endDate);

            assertFalse(result);
        }
    }

    @Test
    void testGetSundayFromWeekDate() {

        LocalDate date = LocalDate.of(2022, 11, 3);
        LocalDate expected = LocalDate.of(2022, 10, 30);

        LocalDate result = WeekCalculatorUtils.getSundayFromWeekDate(date);

        assertEquals(expected, result);
    }

    @Test
    void testGetSundayFromWeekDateWithDateInSunday() {

        LocalDate date = LocalDate.of(2022, 11, 6);
        LocalDate expected = LocalDate.of(2022, 11, 6);

        LocalDate result = WeekCalculatorUtils.getSundayFromWeekDate(date);

        assertEquals(expected, result);
    }

    @Test
    void testGetNextSundayByDate() {

        LocalDate date = LocalDate.of(2022, 11, 3);
        LocalDate expected = LocalDate.of(2022, 11, 6);

        LocalDate result = WeekCalculatorUtils.getNextSundayByDate(date);

        assertEquals(expected, result);
    }

    @Test
    void testGetNextSundayByDateWithDateInSunday() {

        LocalDate date = LocalDate.of(2022, 10, 30);
        LocalDate expected = LocalDate.of(2022, 11, 6);

        LocalDate result = WeekCalculatorUtils.getNextSundayByDate(date);

        assertEquals(expected, result);
    }

    @Test
    void testGetNextSundayByDateWithDateInSaturday() {

        LocalDate date = LocalDate.of(2022, 11, 5);
        LocalDate expected = LocalDate.of(2022, 11, 6);

        LocalDate result = WeekCalculatorUtils.getNextSundayByDate(date);

        assertEquals(expected, result);
    }
}
