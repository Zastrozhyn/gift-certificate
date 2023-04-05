package ru.clevertec.ecl.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.TagDao;
import ru.clevertec.ecl.entity.Tag;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.clevertec.ecl.constant.StringConstant.*;

@Repository
public class TagDaoImpl implements TagDao {

    private final SessionFactory factory;

    private static final String ADD_TAG_TO_CERTIFICATE_QUERY = "INSERT INTO tag_certificate (tag_id, certificate_id) VALUES (?,?)";


    @Autowired
    public TagDaoImpl(SessionFactory factory) {
        this.factory = factory;
    }


    @Override
    public Long create(Tag tag) {
        Session session = factory.openSession();
        session.beginTransaction();
        var savedId = session.save(tag);
        System.out.println(savedId);
        session.getTransaction().commit();
        session.close();
        return (Long) savedId;
    }

    @Override
    public Tag findTag(Long id) {
        Session session = factory.openSession();
        session.beginTransaction();
        var tag = session.find(Tag.class, id);
        session.getTransaction().commit();
        session.close();
        return tag;
    }

    @Override
    public Tag findTagByName(String name) {
        Session session = factory.openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.where(criteriaBuilder.equal(root.get(TAG_NAME), name));
        Tag tag;
        try{
            tag = session.createQuery(query).getSingleResult();
        } catch (NoResultException e){
            return null;
        }

        session.getTransaction().commit();
        session.close();
        return tag;
    }

    @Override
    public List<Tag> findAll(Integer offset, Integer limit) {
        Session session = factory.openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.orderBy(criteriaBuilder.asc(root.get(TAG_ID)));
        var tags = session.createQuery(query).getResultList();
        session.getTransaction().commit();
        session.close();
        return tags;
    }

    @Override
    public void delete(Long id) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.delete(findTag(id));
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void addTagToCertificate(Tag tag, Long idCertificate) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.createNativeQuery(ADD_TAG_TO_CERTIFICATE_QUERY)
                .setParameter(1, new TypedParameterValue(LongType.INSTANCE, tag.getId()))
                .setParameter(2, new TypedParameterValue(LongType.INSTANCE, idCertificate))
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

}