package ru.clevertec.ecl.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final OrderService orderService;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Autowired
    public UserController(UserService service, OrderService orderService) {
        this.service = service;
        this.orderService = orderService;
    }

    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false, defaultValue = "10", name = "pageSize") Integer pageSize,
                                 @RequestParam(required = false, defaultValue = "0", name = "page") Integer page){
        return mapper.mapToDto(service.findAll(pageSize, page));
    }

    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable Long id){
        return mapper.mapToDto(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id,@RequestBody User user){
        return mapper.mapToDto(service.update(user, id));
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto user){
        return mapper.mapToDto(service.create(mapper.mapToEntity(user)));
    }

    @PostMapping("{id}/orders")
    public OrderDto createOrder(@PathVariable Long id, @RequestBody List<Long> certificates ){
        return orderMapper.mapToDto(orderService.create(id, certificates));
    }

    @GetMapping("{id}/orders")
    public List<OrderDto> findAllUsersOrder(@PathVariable Long id){
        return orderMapper.mapToDto(orderService.findAllUsersOrder(id));
    }
}