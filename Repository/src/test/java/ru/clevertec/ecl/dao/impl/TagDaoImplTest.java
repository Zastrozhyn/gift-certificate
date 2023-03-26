package ru.clevertec.ecl.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.config.TestConfig;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("dev")
@Transactional
public class TagDaoImplTest {
    private static final long EXISTING_TAG_ID = 1;
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
    void findByIdTest() {
        Tag actual = tagDao.findTag(EXISTING_TAG_ID);
        assertThat(actual).isEqualTo(expectedTag);
    }

    @Test
    void FindByIdReturnsEmptyWithNonExistingTag() {
        Tag actual = tagDao.findTag(100L);
        assertThat(actual).isEqualTo(null);
    }

    @Test
    void findAllTest() {
        List<Tag> tags = tagDao.findAll();
        assertThat(tags).hasSize(AMOUNT_OF_TAGS_IN_DB);
    }

    @Test
    void findTagsByNameTes() {
        Set<Tag> tags = Set.of(expectedTag);
        List<Tag> tagList = tagDao.findTagsByName(tags);
        assertThat(tagList.get(0)).isEqualTo(expectedTag);
    }

    @Test
    void findByNameTest() {
        Tag actual = tagDao.findTagByName(EXISTING_TAG_NAME);
        assertThat(actual).isEqualTo(expectedTag);
    }

    @Test
    void deleteTest() {
        tagDao.delete(1L);
        List<Tag> tags = tagDao.findAll();
        assertThat(tags).hasSize(AMOUNT_OF_TAGS_IN_DB - 1);
    }
}
