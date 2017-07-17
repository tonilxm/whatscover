package com.whatscover.repository;

import com.whatscover.domain.InsuranceAgency;
import com.whatscover.domain.InsuranceCompany;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InsuranceAgency entity.
 */
@Repository
public interface InsuranceAgencyRepository extends JpaRepository<InsuranceAgency,Long> {
    
	Optional<InsuranceCompany> findOneByCode(String code);
}
