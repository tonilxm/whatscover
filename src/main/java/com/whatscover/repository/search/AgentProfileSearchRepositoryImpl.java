package com.whatscover.repository.search;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.whatscover.config.Constants;
import com.whatscover.domain.AgentProfile;
import com.whatscover.repository.search.ext.AgentProfileSearchExtRepository;

/**
 * Created by minh on 02/08/2017.
 */
public class AgentProfileSearchRepositoryImpl implements AgentProfileSearchExtRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Page<AgentProfile> searchByName(String queryData, Pageable pageable) {
    
        String searchSql = "select ap from AgentProfile ap "
        		+ " where concat(ap.first_name, ap.middle_name, ap.last_name) like :fullName";

        TypedQuery<AgentProfile> typeQueryList = entityManager
        		.createQuery(searchSql, AgentProfile.class)
        		.setParameter("fullName", nameVal(queryData));
    			
        List<AgentProfile> searchResultList = typeQueryList
            .setFirstResult((pageable.getPageSize() * pageable.getPageNumber()))
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        /*String totalRecordSql = "select count(*) from AgentProfile ap "
        		+ " where concat(ap.first_name, ap.middle_name, ap.last_name) like :fullName";

        Query queryList = entityManager.createQuery(totalRecordSql)
        		.setParameter("fullName", nameVal(queryData));

        Long totalRecord =  (Long) queryList.getSingleResult();*/
        Long totalRecord =  Long.valueOf(searchResultList.size());

        return new PageImpl<AgentProfile>(searchResultList, pageable, totalRecord);
    }

	private String nameVal(String value) {
		StringBuilder result = new StringBuilder();
		String[] splitedStrs = value.split("\\s");
		if (splitedStrs.length > 0) {
			for (int i = 0; i < splitedStrs.length; i++) {
				result.append(Constants.PERCENTAGE).append(splitedStrs[i]);
				if (i == (splitedStrs.length - 1)) {
					result.append(Constants.PERCENTAGE);
				}
			}
		} else {
			result.append(paramVal(value));
		}
		
		return result.toString();
	}
	
	protected String paramVal(String str) {
		StringBuilder valueObj = new StringBuilder();
		valueObj.append(Constants.PERCENTAGE).append(str).append(Constants.PERCENTAGE);
		return valueObj.toString();
		
	}
	protected boolean checkEmpty(String str) {
		if ("".equals(str)) {
			return true;
		}
		return false;
	}
}
