package ru.clevertec.ecl.mapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.clevertec.ecl.constant.StringConstant.*;

@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.SSSSSS");

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate certificate = buildCertificate(rs);
        Tag tag = new Tag(rs.getLong(TAG_ID), rs.getString(TAG_NAME));
        certificate.addTag(tag);
        while (rs.next()){
            tag = new Tag(rs.getLong(TAG_ID), rs.getString(TAG_NAME));
            certificate.addTag(tag);
        }
        return certificate;
    }

    public static GiftCertificate buildCertificate(ResultSet rs) throws SQLException {
        GiftCertificate certificate = GiftCertificate.builder()
                .id(rs.getLong(CERTIFICATE_ID))
                .name(rs.getString(CERTIFICATE_NAME))
                .description(rs.getString(CERTIFICATE_DESCRIPTION))
                .price(rs.getBigDecimal(CERTIFICATE_PRICE))
                .duration(Duration.ofDays(rs.getInt(CERTIFICATE_DURATION)))
                .createDate(LocalDateTime.parse(rs.getString(CERTIFICATE_CREATE_DATE),FORMATTER))
                .lastUpdateDate(LocalDateTime.parse(rs.getString(CERTIFICATE_LAST_UPDATE_DATE), FORMATTER))
                .build();
        return certificate;
    }
}