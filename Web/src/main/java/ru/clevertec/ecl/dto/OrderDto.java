package ru.clevertec.ecl.dto;

import lombok.Data;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal cost;
    private User user;
    private List<GiftCertificate> certificateList;
}
