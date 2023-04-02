package br.inatel.thisismeapi.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekCalculatorUtils {

    public static Boolean verifyDateBelongsToCurrentWeek(LocalDate date) {

        LocalDate sundayFromDate = WeekCalculatorUtils.getSundayFromWeekDate(date);
        LocalDate sundayFromCurrentDate = WeekCalculatorUtils.getSundayFromWeekDate(LocalDate.now());

        return sundayFromCurrentDate.isEqual(sundayFromDate);
    }

    public static LocalDate getSundayFromWeekDate(LocalDate date) {

        LocalDate sundayFromDate = date;

        if (DayOfWeek.from(date) != DayOfWeek.SUNDAY) {
            sundayFromDate = sundayFromDate.minusDays(DayOfWeek.from(date).getValue());
        }

        return sundayFromDate;
    }

    public static LocalDate getNextSundayByDate(LocalDate date) {

        int intervalToSunday = DayOfWeek.SUNDAY.getValue() - date.getDayOfWeek().getValue();

        if (intervalToSunday == 0)
            return date.plusDays(DayOfWeek.SUNDAY.getValue());

        return date.plusDays(intervalToSunday);
    }
}
