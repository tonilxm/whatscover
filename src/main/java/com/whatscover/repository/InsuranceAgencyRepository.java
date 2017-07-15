package com.whatscover.repository;

import com.whatscover.domain.InsuranceAgency;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InsuranceAgency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceAgencyRepository extends JpaRepository<InsuranceAgency,Long> {
    
}
