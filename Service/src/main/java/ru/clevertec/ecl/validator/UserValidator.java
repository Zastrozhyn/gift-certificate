package ru.clevertec.ecl.validator;

import org.springframework.stereotype.Component;
import ru.clevertec.ecl.entity.User;

@Component
public class UserValidator {
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MIN_NAME_LENGTH = 2;
    public boolean isValid(User user) {
        String userName = user.getName();
        if (userName != null) {
            int userLength = userName.length();
            return userLength >= MIN_NAME_LENGTH &&
                    userLength <= MAX_NAME_LENGTH;
        }
        return false;
    }
}
