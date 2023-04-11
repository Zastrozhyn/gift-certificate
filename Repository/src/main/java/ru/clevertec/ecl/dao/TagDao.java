package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.Tag;

import java.util.List;

public interface TagDao {
    Long create(Tag tag);

    Tag findTag(Long id);

    Tag findTagByName(String name);

    List<Tag> findAll(Integer offset, Integer limit);

    void delete(Long id);

    void addTagToCertificate(Tag tag, Long idCertificate);

    List<Tag> getMostPopularTag();
}