package ru.clevertec.ecl.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.clevertec.ecl.constant.StringConstant.*;

@Component
public class GiftCertificateExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    @Override
    public List<GiftCertificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, GiftCertificate> giftCertificates = new LinkedHashMap<>();
        while (rs.next()) {
            Long key = rs.getLong(CERTIFICATE_ID);
            giftCertificates.putIfAbsent(key, GiftCertificateMapper.buildCertificate(rs));
            Tag tag = new Tag(rs.getLong(TAG_ID), rs.getString(TAG_NAME));
            giftCertificates.get(key).addTag(tag);
        }
        return new ArrayList<>(giftCertificates.values());
    }
}
