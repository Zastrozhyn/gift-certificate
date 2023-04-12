package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.exception.ExceptionCode;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.validator.TagValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class TagServiceImplTest {
    private static final long TAG_ID = 1;
    private static final String TAG_NAME = "Tag";
    private static int expectedErrorCode;
    private static Tag tag;

    private TagServiceImpl tagService;
    private TagRepository tagDao;
    private TagValidator validator;


    @BeforeAll
    static void init() {
        expectedErrorCode = ExceptionCode.TAG_NOT_FOUND.getErrorCode();
        tag = new Tag(TAG_ID, TAG_NAME);
    }

    @BeforeEach
    public void setUp() {
        tagDao = Mockito.mock(TagRepository.class);
        validator = new TagValidator();
        tagService = new TagServiceImpl(tagDao, validator);
    }

    @AfterEach
    public void afterEachTest() {
        verify(tagDao).findById(TAG_ID);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    void checkCreate() {
        when(tagDao.findByName(TAG_NAME)).thenReturn(null);
        when(tagDao.save(tag)).thenReturn(tag);
        when(tagDao.findById(TAG_ID)).thenReturn(Optional.ofNullable(tag));
        Tag result = tagService.create(tag);
        assertThat(result).isEqualTo(tag);

        verify(tagDao).findByName(TAG_NAME);
        verify(tagDao).save(tag);
    }

    @Test
    void checkFindTag() {
        when(tagDao.findById(TAG_ID)).thenReturn(Optional.ofNullable(tag));
        Tag result = tagService.findTag(TAG_ID);
        assertThat(result).isEqualTo(tag);
    }

    @Test
    void checkFindTagThrowException() {
        when(tagDao.findById(TAG_ID)).thenReturn(null);
        EntityException actualException = assertThrows(EntityException.class, () -> tagService.findTag(TAG_ID));
        assertThat(actualException.getErrorCode()).isEqualTo(expectedErrorCode);
    }

    @Test
    void checkDeleteThrowException() {
        when(tagDao.findById(TAG_ID)).thenReturn(null);
        EntityException actualException = assertThrows(EntityException.class, () -> tagService.delete(TAG_ID));
        assertThat(actualException.getErrorCode()).isEqualTo(expectedErrorCode);
    }
}