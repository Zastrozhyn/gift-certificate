package ru.clevertec.ecl.dto;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.ecl.entity.Tag;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class GiftCertificateDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Long duration;
    private Set<Tag> tags = new LinkedHashSet<>();
}
