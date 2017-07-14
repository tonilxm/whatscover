package com.whatscover.repository;

import com.whatscover.domain.InsuranceProduct;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InsuranceProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct,Long> {
    
}
