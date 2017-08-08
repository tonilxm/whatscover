package com.whatscover.repository.search;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.whatscover.domain.InsuranceProductPremiumRate;
import com.whatscover.repository.search.ext.InsuranceProductPremiumRateSearchExtRepository;

public class InsuranceProductPremiumRateSearchRepositoryImpl implements InsuranceProductPremiumRateSearchExtRepository {

	@PersistenceContext
	private EntityManager entityManager;
	 
	@Override
	public Page<InsuranceProductPremiumRate> searchByInsuranceProductId(Long insuranceProductId, Pageable pageable) {
		String searchSql = "select pr from InsuranceProductPremiumRate pr where pr.insuranceProduct.id = :insuranceProductId";
        List<InsuranceProductPremiumRate> searchResultList = entityManager.createQuery(searchSql, InsuranceProductPremiumRate.class)
            .setParameter("insuranceProductId", insuranceProductId)
            .setFirstResult((pageable.getPageSize() * pageable.getPageNumber()))
            .getResultList();

        String totalRecordSql = "select count(*) from InsuranceProductPremiumRate pr where pr.insuranceProduct.id = :insuranceProductId";
        Long totalRecord =  (Long) entityManager.createQuery(totalRecordSql).setParameter("insuranceProductId", insuranceProductId)
            .getSingleResult();

        Page<InsuranceProductPremiumRate> result = new PageImpl(searchResultList, pageable, totalRecord);

        return result;
	}

}
