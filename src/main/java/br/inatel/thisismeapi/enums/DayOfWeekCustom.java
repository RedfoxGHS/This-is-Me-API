package br.inatel.thisismeapi.enums;

import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;

public enum DayOfWeekCustom implements TemporalAccessor {


    SUNDAY,

    MONDAY,

    TUESDAY,

    WEDNESDAY,

    THURSDAY,

    FRIDAY,

    SATURDAY;

    private static final DayOfWeekCustom[] ENUMS = DayOfWeekCustom.values();

    public static DayOfWeekCustom of(int dayOfWeek) throws DateTimeException {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new DateTimeException("Invalid value for DayOfWeek: " + dayOfWeek);
        }
        return ENUMS[dayOfWeek - 1];
    }

    public static DayOfWeekCustom from(TemporalAccessor temporal) {
        if (temporal instanceof DayOfWeekCustom) {
            return (DayOfWeekCustom) temporal;
        }
        try {
            return of((temporal.get(DAY_OF_WEEK) % 7) + 1);
        } catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain DayOfWeek from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(), ex);
        }
    }

    public int getValue() {
        return ordinal() + 1;
    }


    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField) {
            return field == DAY_OF_WEEK;
        }
        return field != null && field.isSupportedBy(this);
    }

    @Override
    public long getLong(TemporalField field) {
        if (field == DAY_OF_WEEK) {
            return getValue();
        } else if (field instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.getFrom(this);
    }

    public String getDayOfWeekName() {
        return this.name();
    }
}
