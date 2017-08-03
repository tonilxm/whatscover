package com.whatscover.repository.search;

import com.whatscover.domain.InsuranceCompany;
import com.whatscover.repository.search.ext.InsuranceCompanySearchExtRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by toni on 31/07/2017.
 */
public class InsuranceCompanySearchRepositoryImpl implements InsuranceCompanySearchExtRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<InsuranceCompany> searchByName(String name, Pageable pageable) {
        String searchSql = "select ic from InsuranceCompany ic where ic.name like :name";
        List<InsuranceCompany> searchResultList = entityManager.createQuery(searchSql, InsuranceCompany.class)
            .setParameter("name", "%" + name + "%")
            .setFirstResult((pageable.getPageSize() * pageable.getPageNumber()))
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        String totalRecordSql = "select count(*) from InsuranceCompany ic where ic.name like :name";
        Long totalRecord =  (Long) entityManager.createQuery(totalRecordSql).setParameter("name", "%" + name + "%")
            .getSingleResult();

        Page<InsuranceCompany> result = new PageImpl(searchResultList, pageable, totalRecord);

        return result;
    }
}
