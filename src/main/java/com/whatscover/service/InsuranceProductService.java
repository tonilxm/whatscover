package com.whatscover.service;

import com.whatscover.service.dto.InsuranceProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing InsuranceProduct.
 */
public interface InsuranceProductService {

    /**
     * Save a insuranceProduct.
     *
     * @param insuranceProductDTO the entity to save
     * @return the persisted entity
     */
    InsuranceProductDTO save(InsuranceProductDTO insuranceProductDTO);

    /**
     *  Get all the insuranceProducts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InsuranceProductDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" insuranceProduct.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    InsuranceProductDTO findOne(Long id);

    /**
     *  Delete the "id" insuranceProduct.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the insuranceProduct corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InsuranceProductDTO> search(String query, Pageable pageable);
}
