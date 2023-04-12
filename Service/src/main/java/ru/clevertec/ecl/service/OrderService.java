package ru.clevertec.ecl.service;

import ru.clevertec.ecl.entity.Order;

import java.util.List;

public interface OrderService {
    Order create(Long userId, List<Long> certificates);
    Order findById(Long id);
    void delete(Long id);
    List<Order> findAllUsersOrder(Long id);
    Order update(Order order, Long id);
    List<Order> findAll(Integer page, Integer pageSize);
}
