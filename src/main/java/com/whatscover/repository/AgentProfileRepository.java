package com.whatscover.repository;

import com.whatscover.domain.AgentProfile;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AgentProfile entity.
 */
@Repository
public interface AgentProfileRepository extends JpaRepository<AgentProfile,Long> {
	Optional<AgentProfile> findOneByAgentCode(String code);
    Optional<AgentProfile> findOneByEmail(String email);
}
