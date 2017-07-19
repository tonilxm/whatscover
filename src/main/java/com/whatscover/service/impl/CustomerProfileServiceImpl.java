package com.whatscover.service.impl;

import com.whatscover.domain.CustomerProfile;
import com.whatscover.repository.CustomerProfileRepository;
import com.whatscover.repository.search.CustomerProfileSearchRepository;
import com.whatscover.service.CustomerProfileService;
import com.whatscover.service.dto.CustomerProfileDTO;
import com.whatscover.service.mapper.CustomerProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing CustomerProfile.
 */
@Service
@Transactional
public class CustomerProfileServiceImpl implements CustomerProfileService{

    private final Logger log = LoggerFactory.getLogger(CustomerProfileServiceImpl.class);

    private final CustomerProfileRepository customerProfileRepository;

    private final CustomerProfileMapper customerProfileMapper;

    private final CustomerProfileSearchRepository customerProfileSearchRepository;

    public CustomerProfileServiceImpl(CustomerProfileRepository customerProfileRepository, CustomerProfileMapper customerProfileMapper, CustomerProfileSearchRepository customerProfileSearchRepository) {
        this.customerProfileRepository = customerProfileRepository;
        this.customerProfileMapper = customerProfileMapper;
        this.customerProfileSearchRepository = customerProfileSearchRepository;
    }

    /**
     * Save a customerProfile.
     *
     * @param customerProfileDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomerProfileDTO save(CustomerProfileDTO customerProfileDTO) {
        log.debug("Request to save CustomerProfile : {}", customerProfileDTO);
        CustomerProfile customerProfile = customerProfileMapper.toEntity(customerProfileDTO);
        customerProfile = customerProfileRepository.save(customerProfile);
        CustomerProfileDTO result = customerProfileMapper.toDto(customerProfile);
        customerProfileSearchRepository.save(customerProfile);
        return result;
    }

    /**
     *  Get all the customerProfiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerProfiles");
        return customerProfileRepository.findAll(pageable)
            .map(customerProfileMapper::toDto);
    }

    /**
     *  Get one customerProfile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerProfileDTO findOne(Long id) {
        log.debug("Request to get CustomerProfile : {}", id);
        CustomerProfile customerProfile = customerProfileRepository.findOne(id);
        return customerProfileMapper.toDto(customerProfile);
    }

    /**
     *  Delete the  customerProfile by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerProfile : {}", id);
        customerProfileRepository.delete(id);
        customerProfileSearchRepository.delete(id);
    }

    /**
     * Search for the customerProfile corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerProfileDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerProfiles for query {}", query);
        Page<CustomerProfile> result = customerProfileSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(customerProfileMapper::toDto);
    }
}
