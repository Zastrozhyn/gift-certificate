package ru.clevertec.ecl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.exception.ExceptionCode;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.UserService;

import java.util.List;

import static ru.clevertec.ecl.util.PaginationUtil.*;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final UserService userService;
    private final GiftCertificateService certificateService;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, UserService userService, GiftCertificateService certificateService) {
        this.repository = repository;
        this.userService = userService;
        this.certificateService = certificateService;
    }

    @Override
    @Transactional
    public Order create(Long userId, List<Long> certificates) {
        userService.isUserExist(userId);
        Order order = new Order();
        order.setUser(userService.findById(userId));
        order.setCertificateList(certificateService.findAllByIds(certificates));
        return repository.save(order);
    }

    @Override
    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityException(ExceptionCode.ORDER_NOT_FOUND.getErrorCode()));
    }

    @Override
    public List<Order> findAll(Integer page, Integer pageSize) {
        page = checkPage(page);
        pageSize = checkPageSize(pageSize);
        return repository.findAll(PageRequest.of(page,pageSize)).toList();
    }

    @Override
    public void delete(Long id) {
        isOrderExist(id);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Order> findAllUsersOrder(Long userId) {
        userService.isUserExist(userId);
        return repository.findAllByUser(userService.findById(userId));
    }

    @Override
    @Transactional
    public Order update(Order order, Long orderId) {
        isOrderExist(orderId);
        order.setId(orderId);
        return repository.save(order);
    }

    private void isOrderExist (Long orderId){
        if (!repository.existsById(orderId)){
            throw new EntityException(ExceptionCode.ORDER_NOT_FOUND.getErrorCode());
        }
    }
}
