package com.whatscover.repository;

import com.whatscover.domain.InsuranceProductPremiumRate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InsuranceProductPremiumRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceProductPremiumRateRepository extends JpaRepository<InsuranceProductPremiumRate,Long> {
    
}
