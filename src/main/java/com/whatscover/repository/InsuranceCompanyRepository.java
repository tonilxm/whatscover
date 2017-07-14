package com.whatscover.repository;

import com.whatscover.domain.InsuranceCompany;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InsuranceCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany,Long> {
    
}
