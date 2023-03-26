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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.clevertec.ecl.exception.ExceptionCode.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagService tagService;
    private final GiftCertificateValidator giftCertificateValidator;
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_DURATION = "duration";

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
    public List<GiftCertificate> findAll() {
        return giftCertificateDao.findAll();
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
        giftCertificateDao.updateDate(idCertificate);
        return giftCertificateDao.findById(idCertificate);
    }

    @Override
    @Transactional
    public void addTagsToCertificate(Set<Tag> tags, long idCertificate) {
        tagService.createTags(tags);
        List<Tag> newTags = tagService.findTagsByName(tags);
        tagService.addTagsToCertificate(newTags, idCertificate);
    }

    @Override
    @Transactional
    public GiftCertificate deleteTagFromCertificate(Tag tag, long idCertificate) {
        if (isTagCanBeDeletedFromCertificate(tag, idCertificate)) {
            tagService.deleteTagFromCertificate(tagService.findTagByName(tag.getName()), idCertificate);
        }
        giftCertificateDao.updateDate(idCertificate);
        return giftCertificateDao.findById(idCertificate);
    }

    @Override
    public GiftCertificate update(Long id, GiftCertificate updatedGiftCertificate) {
        if (isGiftCertificateValid(updatedGiftCertificate) && isGiftCertificateExist(id)) {
            GiftCertificate currentGiftCertificate = giftCertificateDao.findById(id);
            Map<String, Object> updatedFields = getUpdatedField(updatedGiftCertificate, currentGiftCertificate);
            giftCertificateDao.update(id, updatedFields);
        }
        return giftCertificateDao.findById(id);
    }

    @Override
    public List<GiftCertificate> findByAttributes(String tagName, String searchPart, String sortingField,
                                                  String orderSort, String search) {
        if (search == null) {
            return giftCertificateDao.findAll();
        }
        if (giftCertificateValidator.isGiftCertificateFieldValid(sortingField)
                && giftCertificateValidator.isOrderSortValid(orderSort)) {
            return giftCertificateDao.findByAttributes(tagName, searchPart, sortingField, orderSort);
        }
        throw new EntityException(WRONG_FIND_PARAMETERS.getErrorCode());
    }

    private Map<String, Object> getUpdatedField(GiftCertificate updatedGiftCertificate,
                                                GiftCertificate currentGiftCertificate) {
        Map<String, Object> updatedFields = new HashMap<>();
        if (updatedGiftCertificate.getName() != null &&
                !updatedGiftCertificate.getName().equals(currentGiftCertificate.getName())) {
            updatedFields.put(FIELD_NAME, updatedGiftCertificate.getName());
        }
        if (updatedGiftCertificate.getDescription() != null &&
                !updatedGiftCertificate.getDescription().equals(currentGiftCertificate.getDescription())) {
            updatedFields.put(FIELD_DESCRIPTION, updatedGiftCertificate.getDescription());
        }
        if (updatedGiftCertificate.getDuration().toDays() != 0 &&
                updatedGiftCertificate.getDuration() != currentGiftCertificate.getDuration()) {
            updatedFields.put(FIELD_DURATION, updatedGiftCertificate.getDuration());
        }
        if (updatedGiftCertificate.getPrice() != null &&
                !updatedGiftCertificate.getPrice().equals(currentGiftCertificate.getPrice())) {
            updatedFields.put(FIELD_PRICE, updatedGiftCertificate.getPrice());
        }
        return updatedFields;
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