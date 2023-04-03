package ru.clevertec.ecl.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.config.TestConfig;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;


import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("dev")
@Transactional
public class TagDaoImplTest {
    private static final long EXISTING_TAG_ID = 1;
    private static final int OFFSET = 0;
    private static final int LIMIT = 10;
    private static final Integer AMOUNT_OF_TAGS_IN_DB = 4;
    private static final String EXISTING_TAG_NAME = "IT";
    private final TagDaoImpl tagDao;
    private static Tag expectedTag;

    @Autowired
    public TagDaoImplTest(TagDaoImpl tagDao) {
        this.tagDao = tagDao;
    }

    @BeforeEach
    void setUp(){
        expectedTag = new Tag(EXISTING_TAG_ID, EXISTING_TAG_NAME);
    }

    @Test
    void checkFindById() {
        Tag actual = tagDao.findTag(EXISTING_TAG_ID);
        assertThat(actual).isEqualTo(expectedTag);
    }

    @Test
    void checkFindByIdReturnsEmptyWithNonExistingTag() {
        Tag actual = tagDao.findTag(100L);
        assertThat(actual).isEqualTo(null);
    }

    @Test
    void checkFindAll() {
        List<Tag> tags = tagDao.findAll(OFFSET, LIMIT);
        assertThat(tags).hasSize(AMOUNT_OF_TAGS_IN_DB);
    }


    @Test
    void checkFindByName() {
        Tag actual = tagDao.findTagByName(EXISTING_TAG_NAME);
        assertThat(actual).isEqualTo(expectedTag);
    }


    @Test
    void checkCreate(){
        Tag tag = new Tag(5l,"qwerty");
        tagDao.create(tag);
        assertThat(tagDao.findAll(0,10)).contains(tag);
    }
}
