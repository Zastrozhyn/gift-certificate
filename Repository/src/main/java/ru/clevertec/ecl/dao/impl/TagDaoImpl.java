package ru.clevertec.ecl.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.TagDao;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.mapper.IdMapper;
import ru.clevertec.ecl.util.SqlQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class TagDaoImpl implements TagDao {
    private final JdbcTemplate jdbcTemplate;
    private final IdMapper idMapper;

    private static final String CREATE_TAG_QUERY = "INSERT INTO tag(name) VALUES(?) RETURNING id AS new_id";
    private static final String FIND_TAG_BY_ID_QUERY = "SELECT id, name FROM tag WHERE id = ?";
    private static final String FIND_TAG_BY_NAME_QUERY = "SELECT id, name FROM tag WHERE name = ?";
    private static final String FIND_ALL_TAG_QUERY = "SELECT id, name FROM tag";
    private static final String DELETE_TAG_QUERY = "DELETE FROM tag WHERE id = ?";
    private static final String ADD_TAG_TO_CERTIFICATE_QUERY = "INSERT INTO tag_certificate (tag_id, certificate_id) VALUES (?,?)";
    private static final  String DELETE_TAG_FROM_CERTIFICATE_QUERY = "DELETE FROM tag_certificate WHERE certificate_id=?" +
            "AND tag_id=?";

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, IdMapper idMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.idMapper = idMapper;
    }

    @Override
    public Long create(Tag tag) {
        return jdbcTemplate.queryForObject(CREATE_TAG_QUERY, idMapper, tag.getName());
    }

    @Override
    public Tag findTag(Long id) {
        Tag tag;
        try{
            tag = jdbcTemplate.queryForObject(FIND_TAG_BY_ID_QUERY,
                    new BeanPropertyRowMapper<>(Tag.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return tag;
    }

    @Override
    public Tag findTagByName(String name){
        Tag tag;
        try{
            tag = jdbcTemplate.queryForObject(FIND_TAG_BY_NAME_QUERY, new BeanPropertyRowMapper<>(Tag.class), name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return tag;
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL_TAG_QUERY, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_TAG_QUERY, id);
    }

    @Override
    public void addTagToCertificate(Tag tag, Long idCertificate){
        jdbcTemplate.update(ADD_TAG_TO_CERTIFICATE_QUERY, tag.getId(), idCertificate);
    }

    @Override
    public void addTagSToCertificate(List<Tag> tags, Long idCertificate) {
        String query = SqlQueryBuilder.buildAddTagsToCertificateQuery(tags);
        List<Long> tagsInCertificate = new ArrayList<>();
        for (Tag tag : tags){
            tagsInCertificate.add(tag.getId());
            tagsInCertificate.add(idCertificate);
        }
        jdbcTemplate.update(query, tagsInCertificate.toArray());
    }

    @Override
    public void deleteTagFromCertificate(Tag tag, Long idCertificate){
        jdbcTemplate.update(DELETE_TAG_FROM_CERTIFICATE_QUERY, idCertificate, tag.getId());
    }
    @Override
    public void createTags(Set<Tag> tags) {
        String query = SqlQueryBuilder.buildCreateTagsQuery(tags);
        List<String> names = new ArrayList<>();
        for (Tag tag : tags){
            names.add(tag.getName());
        }
        jdbcTemplate.update(query, names.toArray());
    }

    @Override
    public List<Tag> findTagsByName(Set<Tag> tags) {
        String query = SqlQueryBuilder.buildFindTagsByNameQuery(tags);
        List<String> names = new ArrayList<>();
        for (Tag tag : tags){
            names.add(tag.getName());
        }
        return jdbcTemplate.query(query, names.toArray(), new BeanPropertyRowMapper<>(Tag.class));
    }
}