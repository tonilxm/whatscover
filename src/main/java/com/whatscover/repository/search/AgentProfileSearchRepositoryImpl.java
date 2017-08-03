package com.whatscover.repository.search;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.whatscover.domain.AgentProfile;
import com.whatscover.repository.search.ext.AgentProfileSearchExtRepository;

/**
 * Created by minh on 02/08/2017.
 */
public class AgentProfileSearchRepositoryImpl implements AgentProfileSearchExtRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Page<AgentProfile> searchByName(String []queryData, Pageable pageable) {
    	String condQuery = condQuery(queryData);

        String searchSql = "select ap from AgentProfile ap, InsuranceCompany ic, InsuranceAgency ia "
        		+ " where ic.id = ap.insurance_company_id and ia.id = ap.insurance_agency_id "
        		+ " and ap.first_name like :firstName "
        		+ condQuery;

        TypedQuery<AgentProfile> typeQueryList = entityManager.createQuery(searchSql, AgentProfile.class)
                .setParameter("firstName", valueObj(queryData[0]));
        
        typeQueryList = typeQueryList(queryData, typeQueryList);
    			
        List<AgentProfile> searchResultList = typeQueryList
            .setFirstResult((pageable.getPageSize() * pageable.getPageNumber()))
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        String totalRecordSql = "select count(*) from AgentProfile ap, InsuranceCompany ic, InsuranceAgency ia "
        		+ " where ic.id = ap.insurance_company_id and ia.id = ap.insurance_agency_id "
        		+ " and ap.first_name like :firstName "
        		+ condQuery;

        Query queryList = entityManager.createQuery(totalRecordSql).setParameter("firstName", valueObj(queryData[0]));

        queryList = queryList(queryData, queryList);

        Long totalRecord =  (Long) queryList.getSingleResult();

        Page<AgentProfile> result = new PageImpl(searchResultList, pageable, totalRecord);

        return result;
    }
    
	protected String condQuery(String[] queryData) {
		StringBuilder condQuery = new StringBuilder();
		if (!checkEmpty(queryData[1])) {
			condQuery.append(" and ap.middle_name like :middleName ");
		}
		if (!checkEmpty(queryData[2])) {
			condQuery.append(" and ap.last_name like :lastName ");
		}
		if (!checkEmpty(queryData[3])) {
			condQuery.append(" and ic.name like :companyName ");
		}
		if (!checkEmpty(queryData[4])) {
			condQuery.append(" and ia.name like :agencyName ");
		}
		return condQuery.toString();
	}

	protected TypedQuery<AgentProfile> typeQueryList(String[] queryData, TypedQuery<AgentProfile> typeQueryList) {
		TypedQuery<AgentProfile> typeQueryListTmp = typeQueryList;
		if (!checkEmpty(queryData[1])) {
			typeQueryListTmp = typeQueryListTmp.setParameter("middleName", valueObj(queryData[1]));
		}
		if (!checkEmpty(queryData[2])) {
			typeQueryListTmp = typeQueryListTmp.setParameter("lastName", valueObj(queryData[2]));
		}
		if (!checkEmpty(queryData[3])) {
			typeQueryListTmp = typeQueryListTmp.setParameter("companyName", valueObj(queryData[3]));
		}
		if (!checkEmpty(queryData[4])) {
			typeQueryListTmp = typeQueryListTmp.setParameter("agencyName", valueObj(queryData[4]));
		}
		return typeQueryListTmp;
	}

	protected Query queryList(String[] queryData, Query queryList) {
		Query queryListTmp = queryList;
		if (!checkEmpty(queryData[1])) {
			queryListTmp = queryListTmp.setParameter("middleName", valueObj(queryData[1]));
		}
		if (!checkEmpty(queryData[2])) {
			queryListTmp = queryListTmp.setParameter("lastName", valueObj(queryData[2]));
		}
		if (!checkEmpty(queryData[3])) {
			queryListTmp = queryListTmp.setParameter("companyName", valueObj(queryData[3]));
		}
		if (!checkEmpty(queryData[4])) {
			queryListTmp = queryListTmp.setParameter("agencyName", valueObj(queryData[4]));
		}
		return queryListTmp;
	}

	protected String valueObj(String str) {
		StringBuilder valueObj = new StringBuilder();
		valueObj.append("%").append(str).append("%");
		return valueObj.toString();
		
	}
	protected boolean checkEmpty(String str) {
		if ("".equals(str)) {
			return true;
		}
		return false;
	}
}
