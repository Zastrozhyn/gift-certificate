package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {
    User create(User user);
    User findById(Long id);
    List<User> findAll(Integer offset, Integer limit);
    void delete(Long id);
    User update(User user);
    List<Tag> getMostPopularTag();
    BigDecimal findTotalCost(Long id);
    boolean exists(Long id);
}
