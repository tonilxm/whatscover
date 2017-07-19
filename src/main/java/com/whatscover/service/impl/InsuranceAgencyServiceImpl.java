package com.whatscover.service.impl;

import com.whatscover.service.InsuranceAgencyService;
import com.whatscover.config.Constants;
import com.whatscover.domain.InsuranceAgency;
import com.whatscover.repository.InsuranceAgencyRepository;
import com.whatscover.repository.search.InsuranceAgencySearchRepository;
import com.whatscover.service.dto.InsuranceAgencyDTO;
import com.whatscover.service.mapper.InsuranceAgencyMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InsuranceCompany.
 */
@Service
@Transactional
public class InsuranceAgencyServiceImpl implements InsuranceAgencyService{

    private final Logger log = LoggerFactory.getLogger(InsuranceAgencyServiceImpl.class);

    private final InsuranceAgencyRepository insuranceAgencyRepository;

    private final InsuranceAgencyMapper insuranceAgencyMapper;

    private final InsuranceAgencySearchRepository insuranceAgencySearchRepository;

    public InsuranceAgencyServiceImpl(InsuranceAgencyRepository insuranceAgencyRepository, InsuranceAgencyMapper insuranceAgencyMapper, InsuranceAgencySearchRepository insuranceAgencySearchRepository) {
        this.insuranceAgencyRepository = insuranceAgencyRepository;
        this.insuranceAgencyMapper = insuranceAgencyMapper;
        this.insuranceAgencySearchRepository = insuranceAgencySearchRepository;
    }

    /**
     * Save a insuranceAgencyDTO.
     *
     * @param insuranceAgencyDTO the entity to save
     * @return the persisted entity
     */
	@Override
	public InsuranceAgencyDTO save(InsuranceAgencyDTO insuranceAgencyDTO) {
		log.debug("Request to save InsuranceAgency : {}", insuranceAgencyDTO);
		InsuranceAgency insuranceAgency = insuranceAgencyMapper.toEntity(insuranceAgencyDTO);
		insuranceAgency = insuranceAgencyRepository.save(insuranceAgency);
		InsuranceAgencyDTO result = insuranceAgencyMapper.toDto(insuranceAgency);
		insuranceAgencySearchRepository.save(insuranceAgency);
		return result;
	}

	/**
     *  Get all the insuranceAgencies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
	@Override
	public Page<InsuranceAgencyDTO> findAll(Pageable pageable) {
		log.debug("Request to get all InsuranceAgencies");
        return insuranceAgencyRepository.findAll(pageable)
            .map(insuranceAgencyMapper::toDto);
	}

	/**
     *  Get the "id" insuranceAgency.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
	@Override
    @Transactional(readOnly = true)
	public InsuranceAgencyDTO findOne(Long id) {
        log.debug("Request to get InsuranceAgency : {}", id);
        InsuranceAgency insuranceAgency = insuranceAgencyRepository.findOne(id);
        return insuranceAgencyMapper.toDto(insuranceAgency);
	}

    /**
     *  Delete the "id" insuranceAgency.
     *
     *  @param id the id of the entity
     */
	@Override
	public void delete(Long id) {
        log.debug("Request to delete InsuranceAgency : {}", id);
        insuranceAgencyRepository.delete(id);
        insuranceAgencySearchRepository.delete(id);
	}
	
	/**
     * Search for the insuranceAgency corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
	@Override
    @Transactional(readOnly = true)
	public Page<InsuranceAgencyDTO> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of InsuranceAgencies for query {}", query);
        Page<InsuranceAgency> result = insuranceAgencySearchRepository
        		.search(queryStringQuery(query).field(Constants.INSURANCE_AGENCY_NAME)
				.minimumShouldMatch(String.valueOf(query.length())), pageable);
        return result.map(insuranceAgencyMapper::toDto);
	}
}
