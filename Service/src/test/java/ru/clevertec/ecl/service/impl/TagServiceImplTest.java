package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.ecl.dao.impl.TagDaoImpl;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.exception.ExceptionCode;
import ru.clevertec.ecl.validator.TagValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class TagServiceImplTest {
    private static final long TAG_ID = 1;
    private static final String TAG_NAME = "Tag";
    private static int expectedErrorCode;
    private static Tag tag;

    private TagServiceImpl tagService;
    private TagDaoImpl tagDao;
    private TagValidator validator;


    @BeforeAll
    static void init() {
        expectedErrorCode = ExceptionCode.TAG_NOT_FOUND.getErrorCode();
        tag = new Tag(TAG_ID, TAG_NAME);
    }

    @BeforeEach
    public void setUp() {
        tagDao = Mockito.mock(TagDaoImpl.class);
        validator = new TagValidator();
        tagService = new TagServiceImpl(tagDao, validator);
    }

    @AfterEach
    public void afterEachTest() {
        verify(tagDao).findTag(TAG_ID);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    void checkCreate() {
        when(tagDao.findTagByName(TAG_NAME)).thenReturn(null);
        when(tagDao.create(tag)).thenReturn(tag.getId());
        when(tagDao.findTag(TAG_ID)).thenReturn(tag);
        Tag result = tagService.create(tag);
        assertThat(result).isEqualTo(tag);

        verify(tagDao).findTagByName(TAG_NAME);
        verify(tagDao).create(tag);
    }

    @Test
    void checkFindTag() {
        when(tagDao.findTag(TAG_ID)).thenReturn(tag);
        Tag result = tagService.findTag(TAG_ID);
        assertThat(result).isEqualTo(tag);
    }

    @Test
    void checkFindTagThrowException() {
        when(tagDao.findTag(TAG_ID)).thenReturn(null);
        EntityException actualException = assertThrows(EntityException.class, () -> tagService.findTag(TAG_ID));
        assertThat(actualException.getErrorCode()).isEqualTo(expectedErrorCode);
    }

    @Test
    void checkDeleteThrowException() {
        when(tagDao.findTag(TAG_ID)).thenReturn(null);
        EntityException actualException = assertThrows(EntityException.class, () -> tagService.delete(TAG_ID));
        assertThat(actualException.getErrorCode()).isEqualTo(expectedErrorCode);
    }
}