package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.entity.Tag;

import java.util.List;
import java.util.Set;

public interface TagDao {
    Long create(Tag tag);
    Tag findTag(Long id);
    Tag findTagByName(String name);
    List<Tag> findAll();
    void delete(Long id);
    void addTagToCertificate(Tag tag, Long idCertificate);
    void addTagSToCertificate(List<Tag> tags, Long idCertificate);
    void deleteTagFromCertificate(Tag tag, Long idCertificate);
    void createTags(Set<Tag> tags);
    List<Tag> findTagsByName(Set<Tag> tags);
}