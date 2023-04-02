package br.inatel.thisismeapi.controllers.dtos.responses.converter;

public class DayOfWeekBrazilian {

    public static String convert(String day) {
        switch (day) {
            case "MONDAY":
                return "Segunda-feira";
            case "TUESDAY":
                return "Terça-feira";
            case "WEDNESDAY":
                return "Quarta-feira";
            case "THURSDAY":
                return "Quinta-feira";
            case "FRIDAY":
                return "Sexta-feira";
            case "SATURDAY":
                return "Sábado";
            case "SUNDAY":
                return "Domingo";
            default:
                return "Dia inválido";
        }
    }

}
