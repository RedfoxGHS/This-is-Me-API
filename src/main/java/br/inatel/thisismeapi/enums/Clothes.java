package br.inatel.thisismeapi.enums;

import br.inatel.thisismeapi.exceptions.NotFoundException;

public enum Clothes {

    C1(1L),
    C2(2L);

    private final Long number;

    Clothes(Long id) {
        this.number = id;
    }

    public Long getNumber() {
        return number;
    }

    public static Clothes findById(Long number) {

        if (number == null)
            throw new IllegalArgumentException("numero da roupa não pode ser nulo");

        for (Clothes clothes : Clothes.values()) {
            if (clothes.getNumber().equals(number)) {
                return clothes;
            }
        }

        throw new NotFoundException("Roupa com número [" + number + "] não encontrada!");
    }

    public String getClothesName() {
        return this.name();
    }
}
