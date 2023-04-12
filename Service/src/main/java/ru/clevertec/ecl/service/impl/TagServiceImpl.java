package ru.clevertec.ecl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.validator.TagValidator;

import java.util.List;

import static ru.clevertec.ecl.exception.ExceptionCode.*;
import static ru.clevertec.ecl.util.PaginationUtil.*;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository repository;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagRepository repository, TagValidator tagValidator) {
        this.repository = repository;
        this.tagValidator = tagValidator;
    }

    @Override
    public Tag create(Tag tag) {
        isTagValid(tag);
        return repository.save(tag);
    }

    @Override
    public Tag findTag(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityException(TAG_NOT_FOUND.getErrorCode()));
    }

    @Override
    public List<Tag> findAll(Integer pageSize, Integer page) {
        System.out.println(page);
        page = checkPage(page);
        pageSize = checkPageSize(pageSize);
        System.out.println(page);
        return repository.findAll(PageRequest.of(page,pageSize)).toList();
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else throw new EntityException(TAG_NOT_FOUND.getErrorCode());
    }

    @Override
    public boolean isTagExist(Tag tag) {
        if (!repository.existsById(tag.getId())) {
            throw new EntityException(TAG_NOT_FOUND.getErrorCode());
        }
        return true;
    }


    @Override
    public Tag findTagByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new EntityException(TAG_NOT_FOUND.getErrorCode()));
    }


    @Override
    public boolean isTagValid(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new EntityException(NOT_VALID_TAG_DATA.getErrorCode());
        }
        return true;
    }

    @Override
    public List<Tag> getMostPopularTag(){
        return repository.getMostPopularTag();
    }
}
