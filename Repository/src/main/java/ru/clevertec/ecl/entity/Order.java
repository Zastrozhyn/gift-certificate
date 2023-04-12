package ru.clevertec.ecl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order implements BaseEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_certificates"
            , joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id"))
    private List<GiftCertificate> certificateList;

    public Order(){
        certificateList = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        orderDate = LocalDateTime.now();
        cost = certificateList.stream()
                .map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
