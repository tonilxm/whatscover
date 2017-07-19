package com.whatscover.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.whatscover.service.dto.InsuranceAgencyDTO;

/**
 * Service Interface for managing InsuranceAgency.
 */
public interface InsuranceAgencyService {

    /**
     * Save a insuranceAgencyDTO.
     *
     * @param insuranceAgencyDTO the entity to save
     * @return the persisted entity
     */
	InsuranceAgencyDTO save(InsuranceAgencyDTO insuranceAgencyDTO);

    /**
     *  Get all the insuranceAgencies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InsuranceAgencyDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" insuranceAgency.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    InsuranceAgencyDTO findOne(Long id);

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
    Page<InsuranceAgencyDTO> search(String query, Pageable pageable);
}
