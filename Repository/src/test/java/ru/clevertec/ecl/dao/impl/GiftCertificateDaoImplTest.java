package ru.clevertec.ecl.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.config.TestConfig;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@ActiveProfiles("dev")
public class GiftCertificateDaoImplTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.SSSSSS");
    private static final String TAG_NAME_FOR_SEARCH= "IT";
    private static final String PART_OF_SEARCH = "Descr";
    private static final long TAG_ID = 1;
    private static final String TAG_NAME = "IT";
    private static final long CERTIFICATE_ID = 1;
    private static final String CERTIFICATE_NAME = "Certificate";
    private static final String DESCRIPTION = "Description";
    private static final BigDecimal PRICE = new BigDecimal("1.00");
    private static final int DURATION = 1;
    private static final LocalDateTime CREAT_DATE = LocalDateTime.parse("2021-10-08 11:11:11.100001", FORMATTER);
    private static final LocalDateTime LAST_UPDATE_DATE = LocalDateTime.parse("2021-01-01 01:11:11.100001",FORMATTER);
    private static final int AMOUNT_OF_CERTIFICATES_IN_DB = 3;

    private final GiftCertificateDaoImpl certificateDao;

    private GiftCertificate certificate;

    @Autowired
    GiftCertificateDaoImplTest(GiftCertificateDaoImpl certificateDao) {
        this.certificateDao = certificateDao;
    }

    @BeforeEach
    void setUp() {
        Tag tag = new Tag(TAG_ID, TAG_NAME);
        Set<Tag> tags = new HashSet<>(Set.of(tag, new Tag(3L, "Java")));
        certificate = new GiftCertificate(CERTIFICATE_ID, CERTIFICATE_NAME, DESCRIPTION, PRICE, CREAT_DATE
                , LAST_UPDATE_DATE, Duration.ofDays(DURATION), tags);
    }

    @Test
    void checkFindById() {
        GiftCertificate actual = certificateDao.findById(CERTIFICATE_ID);
        assertThat(actual).isEqualTo(certificate);
    }

    @Test
    void checkFindByIdReturnsEmptyWithNonExistingCertificate() {
        GiftCertificate actual = certificateDao.findById(100L);
        assertThat(actual).isNull();
    }

    @Test
    void checkFindAll() {
        List<GiftCertificate> certificates = certificateDao.findAll();
        assertThat(certificates).hasSize(AMOUNT_OF_CERTIFICATES_IN_DB);
    }

    @Test
    void checkFindByAttributesAmountOfCertificate() {
        List<GiftCertificate> actual = certificateDao.findByAttributes(TAG_NAME_FOR_SEARCH,
                PART_OF_SEARCH, null, null);
        assertThat(actual).hasSize(1);
    }

    @Test
    void checkFindByAttributesTest() {
        List<GiftCertificate> actual = certificateDao.findByAttributes(TAG_NAME_FOR_SEARCH,
                PART_OF_SEARCH, null, null);
        assertThat(actual.get(0)).isEqualTo(certificate);
    }

    @Test
    void checkFindByAttributesNullResult() {
        List<GiftCertificate> actual = certificateDao.findByAttributes(null,
                null, null, null);
        assertThat(actual).hasSize(0);
    }

    @Test
    void checkDeleteNonExistingCertificate() {
        certificateDao.delete(100L);
        List<GiftCertificate> tags = certificateDao.findAll();
        assertThat(tags).hasSize(AMOUNT_OF_CERTIFICATES_IN_DB);
    }

    @Test
    void checkDeleteTest() {
        certificateDao.delete(1L);
        List<GiftCertificate> tags = certificateDao.findAll();
        assertThat(tags).hasSize(AMOUNT_OF_CERTIFICATES_IN_DB - 1);
    }
}

