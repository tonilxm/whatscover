package com.whatscover.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import com.whatscover.service.dto.AgentProfileDTO;

/**
 * Service Interface for managing AgentProfile.
 */
public interface AgentProfileService {

    /**
     * Save a agentProfile.
     *
     * @param agentProfileDTO the entity to save
     * @return the persisted entity
     */
    AgentProfileDTO save(AgentProfileDTO agentProfileDTO);

    /**
     *  Get all the agentProfiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgentProfileDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" agentProfile.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AgentProfileDTO findOne(Long id);

    /**
     *  Delete the "id" agentProfile.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Get the "Email" agentProfile.
     *
     *  @param email the email of the entity
     *  @return the entity
     */
    AgentProfileDTO findOneByEmail(String email);

    /**
     * Search for the agentProfile corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgentProfileDTO> search(String [] queryData, Pageable pageable);
    

    /**
     * Search for the agentProfile by name corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgentProfileDTO> searchByName(String [] queryData, Pageable pageable);

}
