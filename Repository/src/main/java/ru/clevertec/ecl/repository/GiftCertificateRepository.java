package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
    Page<GiftCertificate> findAllByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);
    Page<GiftCertificate> findAllByTagsIn(List<Tag> tags, Pageable pageable);
    List<GiftCertificate> findAllByIdIn(List<Long> ids);
}
