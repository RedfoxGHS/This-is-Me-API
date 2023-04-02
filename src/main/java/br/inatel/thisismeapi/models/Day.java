package br.inatel.thisismeapi.models;

import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;
import java.time.LocalTime;

public class Day implements Comparable<Day> {

    private DayOfWeekCustom dayOfWeek;

    @Schema(example = "00:00")
    private String startTime;

    @Schema(example = "00:00")
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public DayOfWeekCustom getDayOfWeekCustom() {
        return dayOfWeek;
    }

    public void setDayOfWeekCustom(DayOfWeekCustom dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Schema(hidden = true)
    public Long getIntervalInMin() {

        if (startTime != null && endTime != null)
            return Duration.between(LocalTime.parse(this.startTime), LocalTime.parse(this.endTime)).toMinutes();

        return null;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        return dayOfWeek == day.dayOfWeek;
    }

    @Override
    public int compareTo(Day o) {

        if (this.dayOfWeek.getValue() > o.dayOfWeek.getValue()) return 1;

        if (this.dayOfWeek.getValue() < o.dayOfWeek.getValue()) return -1;

        return 0;
    }
}
