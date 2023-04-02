package br.inatel.thisismeapi.controllers.dtos.responses;

import br.inatel.thisismeapi.controllers.dtos.responses.converter.DayOfWeekBrazilian;
import br.inatel.thisismeapi.models.Day;

import java.io.Serializable;
import java.util.Objects;

public class DayResponseDTO implements Serializable {

    private String day;

    public DayResponseDTO(Day day) {

        this.day = DayOfWeekBrazilian.convert(day.getDayOfWeekCustom().name()) + ": " + day.getStartTime() + " - " + day.getEndTime();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DayResponseDTO that = (DayResponseDTO) o;

        return Objects.equals(day, that.day);
    }

    @Override
    public int hashCode() {
        return day != null ? day.hashCode() : 0;
    }
}
