package com.whatscover.repository.search;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.whatscover.domain.InsuranceAgency;
import com.whatscover.repository.search.ext.InsuranceAgencySearchExtRepository;

public class InsuranceAgencySearchRepositoryImpl implements InsuranceAgencySearchExtRepository{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<InsuranceAgency> searchByName(String name, Pageable pageable) {
		 String searchSql = "select ia from InsuranceAgency ia where ia.name like :name";
		 List<InsuranceAgency> searchResultList = entityManager.createQuery(searchSql, InsuranceAgency.class)
				 .setParameter("name", "%" + name + "%")
				 .setFirstResult((pageable.getPageSize() * pageable.getPageNumber()))
				 .setMaxResults(pageable.getPageSize())
				 .getResultList();
 
		 Long totalRecordSql = (long) 0;
		 if (searchResultList.size() > 0) {
			 totalRecordSql = Long.valueOf(searchResultList.size());
		 }
 
         Page<InsuranceAgency> result = new PageImpl<InsuranceAgency>(searchResultList, pageable, totalRecordSql);
 
         return result;
	}

}
