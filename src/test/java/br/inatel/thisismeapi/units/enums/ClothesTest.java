package br.inatel.thisismeapi.units.enums;

import br.inatel.thisismeapi.enums.Clothes;
import br.inatel.thisismeapi.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClothesTest {

    @Test
    void testGetClothesName() {
        Clothes clothes = Clothes.C1;
        String expectedClothesName = "C1";
        String actualClothesName = clothes.getClothesName();
        assertEquals(expectedClothesName, actualClothesName);
    }

    @Test
    void testGetClothesNameWhenClothesIsC2() {
        Clothes clothes = Clothes.C2;
        String expectedClothesName = "C2";
        String actualClothesName = clothes.getClothesName();
        assertEquals(expectedClothesName, actualClothesName);
    }

    @Test
    void testGetNumber() {
        Clothes clothes = Clothes.C1;
        Long expectedNumber = 1L;
        Long actualNumber = clothes.getNumber();
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void testGetNumberWhenClothesIsC2() {
        Clothes clothes = Clothes.C2;
        Long expectedNumber = 2L;
        Long actualNumber = clothes.getNumber();
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    void testFindById() {
        Clothes clothes = Clothes.findById(1L);
        Clothes expectedClothes = Clothes.C1;
        assertEquals(expectedClothes, clothes);
    }

    @Test
    void testFindByIdWhenClothesIsC2() {
        Clothes clothes = Clothes.findById(2L);
        Clothes expectedClothes = Clothes.C2;
        assertEquals(expectedClothes, clothes);
    }

    @Test
    void testFindByIdWhenClothesIsNotFound() {
        assertThrows(NotFoundException.class, () -> Clothes.findById(3L));
    }

    @Test
    void testFindByIdWhenClothesIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Clothes.findById(null));
    }
}
