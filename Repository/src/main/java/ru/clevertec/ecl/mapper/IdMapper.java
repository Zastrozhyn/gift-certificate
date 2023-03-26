package ru.clevertec.ecl.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.clevertec.ecl.constant.StringConstant.NEW_ID;

@Component
public class IdMapper implements RowMapper<Long> {

    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return  rs.getLong(NEW_ID);
    }
}