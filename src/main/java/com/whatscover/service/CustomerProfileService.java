package com.whatscover.service;

import com.whatscover.service.dto.CustomerProfileDTO;
import com.whatscover.service.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CustomerProfile.
 */
public interface CustomerProfileService {

    /**
     * Create customerProfile.
     *
     * @param customerProfileDTO the entity to save
     * @return the persisted entity
     */
    CustomerProfileDTO create(CustomerProfileDTO customerProfileDTO, String randomPassword) throws BusinessException;


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

    /**
     * Delete customer profile by userId
     * @param userId
     */
    void deleteByUserId(Long userId);

    /**
     * Search for the customerProfile by name corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CustomerProfileDTO> searchByName(String query, Pageable pageable);
}
