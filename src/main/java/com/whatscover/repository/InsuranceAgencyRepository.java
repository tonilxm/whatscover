package com.whatscover.repository;

import com.whatscover.domain.InsuranceAgency;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;


/**
 * Spring Data JPA repository for the InsuranceAgency entity.
 */
@Repository
public interface InsuranceAgencyRepository extends JpaRepository<InsuranceAgency,Long> {
    
	Optional<InsuranceAgency> findOneByCode(String code);
}
