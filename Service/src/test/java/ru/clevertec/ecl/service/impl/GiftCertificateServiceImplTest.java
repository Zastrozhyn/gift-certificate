package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.ecl.dao.GiftCertificateDao;
import ru.clevertec.ecl.dao.impl.GiftCertificateDaoImpl;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.exception.ExceptionCode;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.validator.GiftCertificateValidator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class GiftCertificateServiceImplTest {
    private static final long TAG_ID = 1;
    private static final String TAG_NAME = "Tag";
    private static int expectedErrorCode;
    private static final long CERTIFICATE_ID = 1;
    private static final String CERTIFICATE_NAME = "Certificate";
    private static final String CERTIFICATE_NEW_NAME = "new";
    private static final String DESCRIPTION = "Description";
    private static final BigDecimal PRICE = new BigDecimal("100");
    private static final int DURATION = 50;
    private static final LocalDateTime CREATION_DATE = LocalDateTime.now();
    private static final LocalDateTime LAST_UPDATE_DATE = LocalDateTime.now();
    private static Tag tag;
    private static Set<Tag> tagSet;
    private static GiftCertificate certificate;
    private static Map<String,Object> updatedFields;

    private GiftCertificateDao giftCertificateDao;
    private TagService tagService;
    private GiftCertificateValidator giftCertificateValidator;
    private GiftCertificateServiceImpl service;


    @BeforeAll
    static void init(){
        updatedFields = new HashMap<>(Map.of("name", CERTIFICATE_NEW_NAME));
        expectedErrorCode = ExceptionCode.GIFT_CERTIFICATE_NOT_FOUND.getErrorCode();
        tag = new Tag(TAG_ID, TAG_NAME);
        tagSet = new HashSet<>();
        certificate = new GiftCertificate(CERTIFICATE_ID, CERTIFICATE_NAME, DESCRIPTION, PRICE, CREATION_DATE
                , LAST_UPDATE_DATE, Duration.ofDays(DURATION), tagSet);

    }

    @BeforeEach
    void setUp() {
        giftCertificateDao = Mockito.mock(GiftCertificateDaoImpl.class);
        tagService = Mockito.mock(TagServiceImpl.class);
        giftCertificateValidator = new GiftCertificateValidator();
        service = new GiftCertificateServiceImpl(giftCertificateDao, tagService, giftCertificateValidator);
    }

    @AfterEach
    public void afterEachTest(){
        verifyNoMoreInteractions(giftCertificateDao);
    }

    @Test
    void checkFindById() {
        when(giftCertificateDao.findById(CERTIFICATE_ID)).thenReturn(certificate);
        GiftCertificate actualCertificate = service.findById(CERTIFICATE_ID);
        assertThat(actualCertificate).isEqualTo(certificate);
        verify(giftCertificateDao).findById(CERTIFICATE_ID);
    }

    @Test
    void checkFindByIdThrowException() {
        when(giftCertificateDao.findById(CERTIFICATE_ID)).thenReturn(null);
        EntityException actualException = assertThrows(EntityException.class, () -> service.findById(CERTIFICATE_ID));
        assertThat(actualException.getErrorCode()).isEqualTo(expectedErrorCode);
        verify(giftCertificateDao).findById(CERTIFICATE_ID);
    }

    @Test
    void checkDeleteThrowException(){
        when(giftCertificateDao.findById(CERTIFICATE_ID)).thenReturn(null);
        EntityException actualException = assertThrows(EntityException.class, () -> service.delete(CERTIFICATE_ID));
        assertThat(actualException.getErrorCode()).isEqualTo(expectedErrorCode);
        verify(giftCertificateDao).findById(CERTIFICATE_ID);
    }

    @Test
    void checkCreate() {
        when(giftCertificateDao.findById(CERTIFICATE_ID)).thenReturn(certificate);
        when(giftCertificateDao.create(certificate)).thenReturn(CERTIFICATE_ID);
        GiftCertificate actualCertificate = service.create(certificate);
        assertThat(actualCertificate).isEqualTo(certificate);
        verify(giftCertificateDao).findById(CERTIFICATE_ID);
        verify(giftCertificateDao).create(certificate);
    }

    @Test
    void checkAddTagToCertificate(){
        when(giftCertificateDao.findById(CERTIFICATE_ID)).thenReturn(certificate);
        when(tagService.isTagValid(tag)).thenReturn(true);
        when(tagService.findTagByName(TAG_NAME)).thenReturn(tag);
        certificate.addTag(tag);
        GiftCertificate actualCertificate = service.addTagToCertificate(tag, CERTIFICATE_ID);
        assertThat(actualCertificate).isEqualTo(certificate);
        verify(giftCertificateDao,times(2)).findById(CERTIFICATE_ID);
        verify(giftCertificateDao).updateDate(CERTIFICATE_ID);
        verify(tagService).isTagValid(tag);
        verify(tagService, times(2)).findTagByName(TAG_NAME);
    }

}
