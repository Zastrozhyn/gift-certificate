package ru.clevertec.ecl.validator;

import org.springframework.stereotype.Component;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;

import static ru.clevertec.ecl.exception.ExceptionCode.NOT_VALID_TAG_DATA;

@Component
public class TagValidator  {
    private static final int MAX_NAME_LENGTH = 300;
    private static final int MIN_NAME_LENGTH = 2;

    public boolean isValid(Tag tag) {
        String tagName = tag.getName();
        if (tagName != null) {
            int nameLength = tagName.length();
            return nameLength >= MIN_NAME_LENGTH &&
                    nameLength <= MAX_NAME_LENGTH;
        }
        throw new EntityException(NOT_VALID_TAG_DATA.getErrorCode());
    }
}
