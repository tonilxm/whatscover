package com.whatscover.service;

import com.whatscover.service.dto.InsuranceCompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing InsuranceCompany.
 */
public interface InsuranceCompanyService {

    /**
     * Save a insuranceCompany.
     *
     * @param insuranceCompanyDTO the entity to save
     * @return the persisted entity
     */
    InsuranceCompanyDTO save(InsuranceCompanyDTO insuranceCompanyDTO);

    /**
     *  Get all the insuranceCompanies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InsuranceCompanyDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" insuranceCompany.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    InsuranceCompanyDTO findOne(Long id);

    /**
     *  Delete the "id" insuranceCompany.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the insuranceCompany corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InsuranceCompanyDTO> search(String query, Pageable pageable);
}
