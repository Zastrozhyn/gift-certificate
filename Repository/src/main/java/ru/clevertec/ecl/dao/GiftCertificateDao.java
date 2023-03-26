package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDao {
    Long create(GiftCertificate giftCertificate);

    void update(Long id, Map<String, Object> updatedFields);

    List<GiftCertificate> findAll();

    GiftCertificate findById(Long id);

    void delete(Long id);

    List<GiftCertificate> findByAttributes(String tagName, String searchPart, String sortingField, String orderSort);

    void updateDate(Long id);
}
