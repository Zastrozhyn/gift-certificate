package ru.clevertec.ecl.service;

import ru.clevertec.ecl.entity.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    Tag create(Tag tag);

    Tag findTag(Long id);

    List<Tag> findAll(Integer offset, Integer limit);

    void delete(Long id);

    boolean isTagValid(Tag tag);

    boolean isTagExist(Tag tag);

    void addTagToCertificate(Tag tag, Long idCertificate);

    Tag findTagByName(String name);

}