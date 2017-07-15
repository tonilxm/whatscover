package com.whatscover.repository;

import com.whatscover.domain.AgentProfile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AgentProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentProfileRepository extends JpaRepository<AgentProfile,Long> {
    
}
