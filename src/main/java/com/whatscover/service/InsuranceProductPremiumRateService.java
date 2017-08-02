package com.whatscover.service;

import com.whatscover.service.dto.InsuranceProductPremiumRateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing InsuranceProductPremiumRate.
 */
public interface InsuranceProductPremiumRateService {

    /**
     * Save a insuranceProductPremiumRate.
     *
     * @param insuranceProductPremiumRateDTO the entity to save
     * @return the persisted entity
     */
    InsuranceProductPremiumRateDTO save(InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO);

    /**
     *  Get all the insuranceProductPremiumRates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InsuranceProductPremiumRateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" insuranceProductPremiumRate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    InsuranceProductPremiumRateDTO findOne(Long id);

    /**
     *  Delete the "id" insuranceProductPremiumRate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the insuranceProductPremiumRate corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InsuranceProductPremiumRateDTO> search(String query, Pageable pageable);
}
