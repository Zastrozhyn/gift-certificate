package ru.clevertec.ecl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.GiftCertificateDao;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.validator.GiftCertificateValidator;

import java.util.*;

import static ru.clevertec.ecl.exception.ExceptionCode.*;
import static ru.clevertec.ecl.util.PaginationUtil.calculateOffset;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagService tagService;
    private final GiftCertificateValidator giftCertificateValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagService tagService,
                                      GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagService = tagService;
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new EntityException(NOT_VALID_GIFT_CERTIFICATE_DATA.getErrorCode());
        }
        Long newId = giftCertificateDao.create(giftCertificate);
        Set<Tag> tags = giftCertificate.getTags();
        if (giftCertificateValidator.isTagsAttachedToCertificate(tags)) {
            tags.stream().allMatch(tagService::isTagValid);
            addTagsToCertificate(tags, newId);
        }
        return findById(newId);
    }

    @Override
    public List<GiftCertificate> findAll(Integer offset, Integer limit) {
        return giftCertificateDao.findAll(offset, limit);
    }

    @Override
    public GiftCertificate findById(Long id) {
        GiftCertificate giftCertificate = giftCertificateDao.findById(id);
        if (giftCertificate == null) {
            throw new EntityException(GIFT_CERTIFICATE_NOT_FOUND.getErrorCode());
        }
        return giftCertificate;
    }

    @Override
    public void delete(long id) {
        if (isGiftCertificateExist(id)) {
            giftCertificateDao.delete(id);
        }
    }

    @Override
    @Transactional
    public GiftCertificate addTagToCertificate(Tag tag, long idCertificate) {
        if (isGiftCertificateExist(idCertificate) && isTagReadyToCreate(tag)) {
            tagService.create(tag);
        }
        tagService.addTagToCertificate(tagService.findTagByName(tag.getName()), idCertificate);
        return giftCertificateDao.findById(idCertificate);
    }

    @Override
    @Transactional
    public void addTagsToCertificate(Set<Tag> tags, long idCertificate) {
        tags.forEach(tag -> addTagToCertificate(tag, idCertificate));
    }

    @Override
    @Transactional
    public GiftCertificate deleteTagFromCertificate(Tag tag, long idCertificate) {
        GiftCertificate certificate = giftCertificateDao.findById(idCertificate);
        if(isTagCanBeDeletedFromCertificate(tag, idCertificate)){
            Tag deletedTag = tagService.findTagByName(tag.getName());
            certificate.deleteTagFromCertificate(deletedTag);
        }
        return giftCertificateDao.update(certificate);
    }

    @Override
    public GiftCertificate update(Long id, GiftCertificate updatedGiftCertificate) {
        isGiftCertificateExist(id);
        isGiftCertificateValid(updatedGiftCertificate);
        updatedGiftCertificate.setId(id);
        return giftCertificateDao.update(updatedGiftCertificate);
    }

    @Override
    public List<GiftCertificate> findByAttributes(String tagName, String searchPart, String sortingField,
                                                  String orderSort, String search, Integer pageSize, Integer page) {
        List<GiftCertificate> certificates = new ArrayList<>();
        if (search == null){
            certificates = giftCertificateDao.findAll(calculateOffset(pageSize, page), pageSize);
        }
        if (giftCertificateValidator.isGiftCertificateFieldValid(sortingField)
                && giftCertificateValidator.isOrderSortValid(orderSort) && search != null) {
            certificates = giftCertificateDao.findByAttributes(tagName, searchPart, sortingField, orderSort,
                    calculateOffset(pageSize, page), pageSize );
        }
        return certificates;
    }

    private boolean isGiftCertificateValid(GiftCertificate giftCertificate) {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new EntityException(NOT_VALID_GIFT_CERTIFICATE_DATA.getErrorCode());
        }
        return true;
    }

    private boolean isGiftCertificateExist(Long id) {
        if (giftCertificateDao.findById(id) == null) {
            throw new EntityException(GIFT_CERTIFICATE_NOT_FOUND.getErrorCode());
        }
        return true;
    }

    private boolean isTagReadyToCreate(Tag tag) {
        return tagService.isTagValid(tag) && tagService.findTagByName(tag.getName()) == null;
    }

    private boolean isTagCanBeDeletedFromCertificate(Tag tag, long idCertificate) {
        return tagService.isTagValid(tag) && isGiftCertificateExist(idCertificate) && tagService.isTagExist(tag);
    }
}