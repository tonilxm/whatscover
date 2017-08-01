package com.whatscover.repository.search;

import com.whatscover.domain.CustomerProfile;
import com.whatscover.repository.search.ext.CustomerProfileSearchExtRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by toni on 01/08/2017.
 */
public class CustomerProfileSearchRepositoryImpl implements CustomerProfileSearchExtRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CustomerProfile> searchByName(String name, Pageable pageable) {
        String searchSql = "select cp from CustomerProfile cp where cp.firstName like :name";
        List<CustomerProfile> searchResultList = entityManager.createQuery(searchSql, CustomerProfile.class)
            .setParameter("name", "%" + name + "%")
            .setFirstResult((pageable.getPageSize() * pageable.getPageNumber()))
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        String totalRecordSql = "select count(*) from CustomerProfile cp where cp.name like :name";
        Long totalRecord =  (Long) entityManager.createQuery(totalRecordSql).setParameter("name", "%" + name + "%")
            .getSingleResult();

        Page<CustomerProfile> result = new PageImpl(searchResultList, pageable, totalRecord);

        return result;
    }
}
