package com.whatscover.repository;

import com.whatscover.domain.InsuaranceAgency;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InsuaranceAgency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuaranceAgencyRepository extends JpaRepository<InsuaranceAgency,Long> {
    
}
