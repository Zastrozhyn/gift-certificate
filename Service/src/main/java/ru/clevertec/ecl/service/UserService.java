package ru.clevertec.ecl.service;

import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User create(User user);

    User findById(Long id);

    List<User> findAll(Integer pageSize, Integer page);

    void delete(Long id);

    User update(User user, Long id);

    boolean isUserExist(Long userId);
}
