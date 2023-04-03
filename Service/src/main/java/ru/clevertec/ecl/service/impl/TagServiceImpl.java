package ru.clevertec.ecl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.TagDao;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.validator.TagValidator;

import java.util.List;

import static ru.clevertec.ecl.exception.ExceptionCode.*;
import static ru.clevertec.ecl.util.PaginationUtil.calculateOffset;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    @Override
    public Tag create(Tag tag) {
        if (isTagValid(tag) && tagDao.findTagByName(tag.getName()) == null) {
            Long idCreatedTag = tagDao.create(tag);
            return findTag(idCreatedTag);
        }
        return findTagByName(tag.getName());
    }

    @Override
    public Tag findTag(Long id) {
        Tag tag = tagDao.findTag(id);
        if (tag == null) {
            throw new EntityException(TAG_NOT_FOUND.getErrorCode());
        }
        return tag;
    }

    @Override
    public List<Tag> findAll(Integer pageSize, Integer page) {
        return tagDao.findAll(calculateOffset(pageSize,page), pageSize);
    }

    @Override
    public void delete(Long id) {
        if (tagDao.findTag(id) != null) {
            tagDao.delete(id);
        } else throw new EntityException(TAG_NOT_FOUND.getErrorCode());
    }

    @Override
    public boolean isTagExist(Tag tag) {
        if (tagDao.findTagByName(tag.getName()) == null) {
            throw new EntityException(TAG_NOT_FOUND.getErrorCode());
        }
        return true;
    }

    @Override
    public void addTagToCertificate(Tag tag, Long idCertificate) {
        tagDao.addTagToCertificate(tag, idCertificate);
    }

    @Override
    public Tag findTagByName(String name) {
        return tagDao.findTagByName(name);
    }


    @Override
    public boolean isTagValid(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new EntityException(NOT_VALID_TAG_DATA.getErrorCode());
        }
        return true;
    }
}
