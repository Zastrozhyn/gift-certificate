package ru.clevertec.ecl.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.clevertec.ecl.exception.EntityException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateValidatorTest {
    private static GiftCertificateValidator validator;

    @BeforeAll
    static void setUp() {
        validator = new GiftCertificateValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "another-name", "QWERTY", "qa"})
    void checkIsNameValidTest(String name) {
        assertTrue(validator.isNameValid(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"sport", "anything", "QWERTY", "qa"})
    void checkIsDescriptionValidTest(String description) {
        assertTrue(validator.isDescriptionValid(description));
    }

    @ParameterizedTest
    @ValueSource(doubles = {100, 1000, 1.1, 5})
    void checkIsPriceValidTest(double price) {
        assertTrue(validator.isPriceValid(BigDecimal.valueOf(price)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-100, 1000000, 0})
    void checkIsPriceNotValidTest(double price) {
        assertThrows(EntityException.class, () -> validator.isPriceValid(BigDecimal.valueOf(price)));
    }

    @ParameterizedTest
    @ValueSource(longs = {100, 1, 5})
    void checkIsDurationValid(Long duration) {
        assertTrue(validator.isDurationValid(duration));
    }

    @Test
    void checkIsDescriptionValidThrowWithUpdatedMoreThen301SymbolsDescription() {
        String MoreThen301Description = "D".repeat(301);
        assertThrows(EntityException.class, () -> validator.isDescriptionValid(MoreThen301Description));
    }

    @ParameterizedTest
    @ValueSource(strings = {"asc", "desc", "ASC", "DESC", "desC", "AsC"})
    void checkIsOrderSortValidReturnsTrue(String orderSort) {
        assertTrue(validator.isOrderSortValid(orderSort));
    }

    @ParameterizedTest
    @ValueSource(strings = {"name", "duration", "price", "create_date"})
    void checkIsGiftCertificateFieldListValidReturnsTrueWithValidParams(String field) {
        assertTrue(validator.isGiftCertificateFieldValid(field));
    }

}