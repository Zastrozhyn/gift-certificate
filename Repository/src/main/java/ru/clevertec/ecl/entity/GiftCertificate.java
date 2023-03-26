package ru.clevertec.ecl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Duration duration;
    @Builder.Default
    private Set<Tag> tags = new LinkedHashSet<>();

    public void addTag(Tag tag) {
        tags.add(tag);
    }
}
