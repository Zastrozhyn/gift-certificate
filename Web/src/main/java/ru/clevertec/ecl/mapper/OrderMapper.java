package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.entity.Order;

import java.util.Collection;
import java.util.List;

@Mapper
public interface OrderMapper {
    Order mapToEntity(OrderDto model);

    OrderDto mapToDto(Order entity);

    List<OrderDto> mapToDto(Collection<Order> entities);
}
