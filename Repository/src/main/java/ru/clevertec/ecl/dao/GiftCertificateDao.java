package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDao {
    Long create(GiftCertificate giftCertificate);

    GiftCertificate update(GiftCertificate giftCertificate);

    List<GiftCertificate> findAll(Integer offset, Integer limit);

    GiftCertificate findById(Long id);

    void delete(Long id);

    List<GiftCertificate> findByAttributes(String tagName, String searchPart, String sortingField, String orderSort,
                                           Integer offset, Integer limit);
    List<GiftCertificate> findAllByIds(List<Long> idList);

}
