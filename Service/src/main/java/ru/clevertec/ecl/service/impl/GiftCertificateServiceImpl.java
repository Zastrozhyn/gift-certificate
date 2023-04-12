package ru.clevertec.ecl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.exception.ExceptionCode;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.validator.GiftCertificateValidator;

import java.util.*;

import static ru.clevertec.ecl.exception.ExceptionCode.*;
import static ru.clevertec.ecl.util.PaginationUtil.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository repository;
    private final TagService tagService;
    private final GiftCertificateValidator giftCertificateValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository repository, TagService tagService,
                                      GiftCertificateValidator giftCertificateValidator) {
        this.repository = repository;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagService = tagService;
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new EntityException(NOT_VALID_GIFT_CERTIFICATE_DATA.getErrorCode());
        }
        return repository.save(giftCertificate);
    }

    @Override
    public List<GiftCertificate> findAll(Integer page, Integer pageSize) {
        page = checkPage(page);
        pageSize = checkPageSize(pageSize);
        return repository.findAll(PageRequest.of(page,pageSize)).toList();
    }

    @Override
    public GiftCertificate findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityException(GIFT_CERTIFICATE_NOT_FOUND.getErrorCode()));
    }

    @Override
    public void delete(long id) {
        if (isGiftCertificateExist(id)) {
            repository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public GiftCertificate addTagToCertificate(Tag tag, long idCertificate) {
        if (isGiftCertificateExist(idCertificate) && isTagReadyToCreate(tag)) {
            tagService.create(tag);
        }
        GiftCertificate certificate = findById(idCertificate);
        Tag tag1 = tagService.findTagByName(tag.getName());
        certificate.addTag(tag1);
        return repository.save(certificate);
    }

    @Override
    @Transactional
    public void addTagsToCertificate(Set<Tag> tags, long idCertificate) {
        tags.forEach(tag -> addTagToCertificate(tag, idCertificate));
    }

    @Override
    @Transactional
    public GiftCertificate deleteTagFromCertificate(Tag tag, long idCertificate) {
        GiftCertificate certificate = findById(idCertificate);
        if(isTagCanBeDeletedFromCertificate(tag, idCertificate)){
            Tag deletedTag = tagService.findTagByName(tag.getName());
            certificate.deleteTagFromCertificate(deletedTag);
        }
        return repository.save(certificate);
    }

    @Override
    public GiftCertificate update(Long id, GiftCertificate updatedGiftCertificate) {
        GiftCertificate certificate = findById(id);
        if(!certificate.getName().equals(updatedGiftCertificate.getName())
                || !certificate.getDescription().equals(updatedGiftCertificate.getDescription())){
            throw new EntityException(ERROR_INPUT_DATA.getErrorCode());
        }
        isGiftCertificateExist(id);
        isGiftCertificateValid(updatedGiftCertificate);
        updatedGiftCertificate.setId(id);
        return repository.save(updatedGiftCertificate);
    }

    @Override
    public List<GiftCertificate> findByAttributes(String tagName, String searchPart, String sortingField,
                                                  String orderSort, String search, Integer pageSize, Integer page) {
        page = checkPage(page);
        pageSize = checkPageSize(pageSize);
        List<GiftCertificate> certificates = new ArrayList<>();
        if (search == null){
            certificates = findAll(page, pageSize);
        }
        if (giftCertificateValidator.isGiftCertificateFieldValid(sortingField)
                && giftCertificateValidator.isOrderSortValid(orderSort) && search != null) {
            Sort sort;
            if (sortingField != null){
                sort = Sort.by(sortingField);
            } else {
                sort = Sort.by("name");
            }
            certificates = repository.findAllByNameContainingOrDescriptionContaining(searchPart, searchPart, PageRequest.of(page,pageSize,sort)).toList();
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
        if (!repository.existsById(id)) {
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

    @Override
    public List<GiftCertificate> findAllByIds(List<Long> idList) {
        return repository.findAllByIdIn(idList);
    }

}