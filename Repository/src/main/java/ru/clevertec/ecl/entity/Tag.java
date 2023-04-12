package ru.clevertec.ecl.entity;

import lombok.*;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "tag")
public class Tag implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
