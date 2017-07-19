package com.whatscover.service;

import com.whatscover.service.dto.CustomerProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CustomerProfile.
 */
public interface CustomerProfileService {

    /**
     * Save a customerProfile.
     *
     * @param customerProfileDTO the entity to save
     * @return the persisted entity
     */
    CustomerProfileDTO save(CustomerProfileDTO customerProfileDTO);

    /**
     *  Get all the customerProfiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CustomerProfileDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" customerProfile.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CustomerProfileDTO findOne(Long id);

    /**
     *  Delete the "id" customerProfile.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the customerProfile corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CustomerProfileDTO> search(String query, Pageable pageable);
}
