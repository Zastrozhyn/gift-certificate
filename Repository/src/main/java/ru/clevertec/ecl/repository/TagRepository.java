package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    String GET_MOST_POPULAR_TAG_OF_RICHEST_USER = """
        SELECT t.id, t.name FROM tag as t
                JOIN tag_certificate as tc ON tc.tag_id=t.id
                JOIN order_certificates as oc ON oc.certificate_id=tc.certificate_id
                JOIN orders as o ON o.id=oc.order_id AND o.user_id=(
                SELECT u.id FROM users as u
                JOIN orders as ord ON ord.user_id=u.id
                GROUP BY u.id ORDER BY sum(ord.cost) DESC LIMIT 1
                ) GROUP BY t.id HAVING count(t.id) =
        (SELECT max(tagsCount) FROM (SELECT count(t.id) AS tagsCount FROM tag as t
                JOIN tag_certificate as tc ON tc.tag_id=t.id
                JOIN order_certificates as oc ON oc.certificate_id=tc.certificate_id
                JOIN orders as o ON o.id=oc.order_id AND o.user_id=(
                SELECT u.id FROM users as u
                JOIN orders as ord ON ord.user_id=u.id
                GROUP BY u.id ORDER BY sum(ord.cost) DESC LIMIT 1
                ) GROUP BY t.id) AS tagsc)""";


    List<Tag> findByName(String name);

    @Query(value = GET_MOST_POPULAR_TAG_OF_RICHEST_USER, nativeQuery = true)
    List<Tag> getMostPopularTag();
}
