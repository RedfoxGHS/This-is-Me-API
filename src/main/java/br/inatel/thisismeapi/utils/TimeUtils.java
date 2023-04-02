package br.inatel.thisismeapi.utils;

public class TimeUtils {

    /**
     * timeInMin = duration beetween two hours in minutes
     *
     * @return will return a String in format as 1h45m
     */
    public static String getTimeInTextFormat(Long timeInMin) {
        Long hour = timeInMin / 60L;
        Long min = timeInMin % 60L;

        if (hour == 0)
            return min + "min";

        return hour + "h" + min + "m";
    }
}
