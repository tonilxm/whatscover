package com.whatscover.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import com.whatscover.service.dto.AgentProfileDTO;

/**
 * Service Interface for managing InsuranceAgency.
 */
public interface AgentProfileService {

    /**
     * Save a insuranceAgencyDTO.
     *
     * @param insuranceAgencyDTO the entity to save
     * @return the persisted entity
     */
	AgentProfileDTO save(AgentProfileDTO agentProfileDTO);

    /**
     *  Get all the insuranceAgencies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgentProfileDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" insuranceAgency.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AgentProfileDTO findOne(Long id);

    /**
     *  Delete the "id" insuranceAgency.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the insuranceAgency corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgentProfileDTO> search(String [] queryData, Pageable pageable);
}
