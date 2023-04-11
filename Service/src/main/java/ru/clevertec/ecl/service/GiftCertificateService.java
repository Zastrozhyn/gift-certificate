package ru.clevertec.ecl.service;

import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;
import java.util.Set;

public interface GiftCertificateService {
    GiftCertificate create(GiftCertificate giftCertificate);

    List<GiftCertificate> findAll(Integer offset, Integer limit);

    GiftCertificate findById(Long id);

    void delete(long id);

    GiftCertificate addTagToCertificate(Tag tag, long idCertificate);

    void addTagsToCertificate(Set<Tag> tags, long idCertificate);

    GiftCertificate deleteTagFromCertificate(Tag tag, long idCertificate);

    GiftCertificate update(Long id, GiftCertificate giftCertificate);

    List<GiftCertificate> findByAttributes(String tagName, String searchPart, String sortingField,
                                           String orderSort, String search, Integer pageSize, Integer page);
    List<GiftCertificate> findAllByIds(List<Long> idList);

    void addTagToCertificate(Tag tag, Long idCertificate);
}
