package ru.clevertec.ecl.util;

import ru.clevertec.ecl.entity.Tag;

import java.util.List;
import java.util.Set;

public class SqlQueryBuilder {
    public static String DESC_SORT = "DESC";
    public static String CERTIFICATE_ID = " gift_certificate.id";
    public static final String START_OF_UPDATE_QUERY = "UPDATE gift_certificate SET ";
    public static final String MIDDLE_OF_UPDATE_QUERY = "%s=?, ";
    public static final String END_OF_UPDATE_QUERY = "%s=? WHERE id=?";
    private static final String EMPTY = "NULL";
    private static final String ADD_VALUES = ", (?)";
    private static final String ADD_TWO_VALUES = ", (?,?)";
    public static final String END_OF_CREATE_TAGS_QUERY = " ON CONFLICT DO NOTHING";
    public static final String SEARCH_AND_SORT_QUERY = "SELECT gift_certificate.id, gift_certificate.name" +
            ", description, price, duration, create_date, last_update_date, tag.id AS tag_id, tag.name AS tag_name " +
            "FROM gift_certificate " +
            " JOIN tag_certificate ON certificate_id=gift_certificate.id " +
            "JOIN tag ON tag_id=tag.id " +
            "WHERE tag.name LIKE CONCAT ('%%', '%s', '%%') OR (gift_certificate.name LIKE CONCAT ('%%', '%s', '%%') " +
            "OR gift_certificate.description LIKE CONCAT ('%%', '%s', '%%')) ORDER BY ";
    private static final String CREATE_TAGS_QUERY = "INSERT INTO tag(name) VALUES(?)";
    private static final String FIND_TAGS_QUERY = "SELECT id, name from tag WHERE name=?";
    public static final String END_OF_FIND_TAGS_QUERY = " OR name=?";
    private static final String ADD_TAGS_TO_CERTIFICATE = "INSERT INTO tag_certificate (tag_id, certificate_id) VALUES (?,?)";

    public static String buildCertificateQueryForUpdate(Set<String> columnNames) {
        String res = START_OF_UPDATE_QUERY + MIDDLE_OF_UPDATE_QUERY.repeat(columnNames.size()-1) + END_OF_UPDATE_QUERY;
        return res.formatted(columnNames.toArray());
    }

    public static String buildCertificateQueryForSearchAndSort(String tagName, String searchPart,
                                                               String sortingField, String orderSort) {
        tagName = tagName != null ? tagName : EMPTY;
        searchPart = searchPart != null ? searchPart : EMPTY;
        String queryMainPart = String.format(SEARCH_AND_SORT_QUERY, tagName, searchPart, searchPart);
        StringBuilder resultQuery = new StringBuilder(queryMainPart);

        if (sortingField != null && !sortingField.isEmpty()) {
            resultQuery.append(sortingField);
        } else {
            resultQuery.append(CERTIFICATE_ID);
        }
        if (orderSort != null && orderSort.equalsIgnoreCase(DESC_SORT)){
            resultQuery.append(" ").append(DESC_SORT);
        }
        return resultQuery.toString();
    }

    public static String buildCreateTagsQuery(Set<Tag> tags){
        return CREATE_TAGS_QUERY + ADD_VALUES.repeat(tags.size()-1) + END_OF_CREATE_TAGS_QUERY;
    }

    public static String buildFindTagsByNameQuery(Set<Tag> tags){
        return FIND_TAGS_QUERY+ END_OF_FIND_TAGS_QUERY.repeat(tags.size()-1);
    }

    public static String buildAddTagsToCertificateQuery(List<Tag> tags){
        return ADD_TAGS_TO_CERTIFICATE+ ADD_TWO_VALUES.repeat(tags.size()-1);
    }
}
