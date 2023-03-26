package ru.clevertec.ecl.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;

import static org.junit.jupiter.api.Assertions.*;

class TagValidatorTest {

    private static TagValidator validator;
    private static final String NOT_VALID_NAME = "a";

    @BeforeAll
    static void setUp() {
        validator = new TagValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "Tag-name", "another-tag-name", "QWERTY", "qa"})
    void checkIsTagValid(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        assertTrue(validator.isValid(tag));
    }

    @Test
    void checkIsTagNotValid() {
        Tag tag = new Tag();
        tag.setName(NOT_VALID_NAME);
        assertFalse(validator.isValid(tag));
    }
}
