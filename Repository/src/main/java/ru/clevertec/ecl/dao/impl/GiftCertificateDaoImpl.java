package ru.clevertec.ecl.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.GiftCertificateDao;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.util.SqlQueryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

import static ru.clevertec.ecl.constant.StringConstant.CERTIFICATE_ID;


@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private final SessionFactory factory;

    @Autowired
    public GiftCertificateDaoImpl(SessionFactory factory) {
        this.factory = factory;
    }


    @Override
    public Long create(GiftCertificate giftCertificate) {
        Session session = factory.openSession();
        session.beginTransaction();
        var savedId = session.save(giftCertificate);
        session.getTransaction().commit();
        session.close();
        return (Long) savedId;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        Session session = factory.openSession();
        session.beginTransaction();
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        session.update(giftCertificate);
        session.getTransaction().commit();
        session.close();
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> findAll(Integer offset, Integer limit) {
        Session session = factory.openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        query.orderBy(criteriaBuilder.asc(root.get(CERTIFICATE_ID)));
        List<GiftCertificate> result = session.createQuery(query)
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public GiftCertificate findById(Long id) {
        Session session = factory.openSession();
        session.beginTransaction();
        var certificate = session.find(GiftCertificate.class, id);
        session.getTransaction().commit();
        session.close();
        return certificate;
    }

    @Override
    public void delete(Long id) {
        Session session = factory.openSession();
        session.beginTransaction();
        GiftCertificate deletedCertificate = session.find(GiftCertificate.class, id);
        if (deletedCertificate == null){
            return;
        }
        session.delete(deletedCertificate);
        session.getTransaction().commit();
        session.close();
    }


    @Override
    public List<GiftCertificate> findByAttributes(String tagName, String searchPart, String sortingField,
                                                  String orderSort, Integer offset, Integer limit) {
        String query = SqlQueryBuilder
                .buildCertificateQueryForSearchAndSort(tagName, searchPart, sortingField, orderSort);
        Session session = factory.openSession();
        session.beginTransaction();
        List<GiftCertificate> result = session.createNativeQuery(query, GiftCertificate.class)
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }

}