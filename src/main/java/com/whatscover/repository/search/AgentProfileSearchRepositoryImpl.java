package com.whatscover.repository.search;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.whatscover.domain.AgentProfile;
import com.whatscover.domain.CustomerProfile;
import com.whatscover.repository.search.ext.AgentProfileSearchExtRepository;

/**
 * Created by minh on 02/08/2017.
 */
public class AgentProfileSearchRepositoryImpl implements AgentProfileSearchExtRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<AgentProfile> searchByName(String []queryData, Pageable pageable) {
        String searchSql = "select ap from AgentProfile ap, InsuranceCompany ic, InsuranceAgency ia "
        		+ " where ic.id = ap.insurance_company_id and ia.id = ap.insurance_agency_id "
        		+ " and ap.first_name like :firstName and ap.middle_name like :middleName and ap.last_name like :lastName "
        		+ " and ic.name like :companyName "
        		+ " and ia.name like :agencyName ";

        List<AgentProfile> searchResultList = entityManager.createQuery(searchSql, AgentProfile.class)
            .setParameter("firstName", "%" + queryData[0] + "%")
            .setParameter("middleName", "%" + queryData[1] + "%")
            .setParameter("lastName", "%" + queryData[2] + "%")
            .setParameter("companyName", "%" + queryData[3] + "%")
            .setParameter("agencyName", "%" + queryData[4] + "%")
            .setFirstResult((pageable.getPageSize() * pageable.getPageNumber()))
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        String totalRecordSql = "select count(*) from AgentProfile ap, InsuranceCompany ic, InsuranceAgency ia "
        		+ " where ic.id = ap.insurance_company_id and ia.id = ap.insurance_agency_id "
        		+ " and ap.first_name like :firstName and ap.middle_name like :middleName and ap.last_name like :lastName "
        		+ " and ic.name like :companyName "
        		+ " and ia.name like :agencyName ";

        Long totalRecord =  (Long) entityManager.createQuery(totalRecordSql).setParameter("firstName", "%" + queryData[0] + "%")
                .setParameter("middleName", "%" + queryData[1] + "%")
                .setParameter("lastName", "%" + queryData[2] + "%")
                .setParameter("companyName", "%" + queryData[3] + "%")
                .setParameter("agencyName", "%" + queryData[4] + "%")
                .getSingleResult();

        Page<AgentProfile> result = new PageImpl(searchResultList, pageable, totalRecord);

        return result;
    }
}
