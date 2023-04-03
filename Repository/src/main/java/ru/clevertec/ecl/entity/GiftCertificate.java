package ru.clevertec.ecl.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "gift_certificate")
public class GiftCertificate implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "duration")
    private Duration duration;

    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tag_certificate",
            joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @PrePersist
    public void prePersist() {
        createDate = LocalDateTime.now();
        lastUpdateDate = createDate;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void deleteTagFromCertificate(Tag deletedTag) {
        tags.remove(deletedTag);
    }
}
