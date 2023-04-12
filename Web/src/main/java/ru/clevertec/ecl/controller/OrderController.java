package ru.clevertec.ecl.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;
    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderDto> findAll(@RequestParam(required = false, defaultValue = "10", name = "pageSize") Integer pageSize,
                                  @RequestParam(required = false, defaultValue = "0", name = "page") Integer page){
        return mapper.mapToDto(service.findAll(page, pageSize));
    }

    @GetMapping("/{id}")
    public OrderDto findOrder(@PathVariable Long id){
        return mapper.mapToDto(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public OrderDto update(@PathVariable Long id,@RequestBody Order order){
        return mapper.mapToDto(service.update(order, id));
    }

}
