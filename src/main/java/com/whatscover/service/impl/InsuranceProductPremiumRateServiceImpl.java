package com.whatscover.service.impl;

import com.whatscover.service.InsuranceProductPremiumRateService;
import com.whatscover.domain.InsuranceProductPremiumRate;
import com.whatscover.domain.enumeration.ProductPremiumRateEntityStatus;
import com.whatscover.repository.InsuranceProductPremiumRateRepository;
import com.whatscover.repository.search.InsuranceProductPremiumRateSearchRepository;
import com.whatscover.service.dto.InsuranceProductPremiumRateDTO;
import com.whatscover.service.mapper.InsuranceProductPremiumRateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

/**
 * Service Implementation for managing InsuranceProductPremiumRate.
 */
@Service
@Transactional
public class InsuranceProductPremiumRateServiceImpl implements InsuranceProductPremiumRateService{

    private final Logger log = LoggerFactory.getLogger(InsuranceProductPremiumRateServiceImpl.class);

    private final InsuranceProductPremiumRateRepository insuranceProductPremiumRateRepository;

    private final InsuranceProductPremiumRateMapper insuranceProductPremiumRateMapper;

    private final InsuranceProductPremiumRateSearchRepository insuranceProductPremiumRateSearchRepository;

    public InsuranceProductPremiumRateServiceImpl(InsuranceProductPremiumRateRepository insuranceProductPremiumRateRepository, InsuranceProductPremiumRateMapper insuranceProductPremiumRateMapper, InsuranceProductPremiumRateSearchRepository insuranceProductPremiumRateSearchRepository) {
        this.insuranceProductPremiumRateRepository = insuranceProductPremiumRateRepository;
        this.insuranceProductPremiumRateMapper = insuranceProductPremiumRateMapper;
        this.insuranceProductPremiumRateSearchRepository = insuranceProductPremiumRateSearchRepository;
    }

    /**
     * Save a insuranceProductPremiumRate.
     *
     * @param insuranceProductPremiumRateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InsuranceProductPremiumRateDTO save(InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO) {
        log.debug("Request to save InsuranceProductPremiumRate : {}", insuranceProductPremiumRateDTO);
        InsuranceProductPremiumRate insuranceProductPremiumRate = insuranceProductPremiumRateMapper.toEntity(insuranceProductPremiumRateDTO);
        insuranceProductPremiumRate = insuranceProductPremiumRateRepository.save(insuranceProductPremiumRate);
        InsuranceProductPremiumRateDTO result = insuranceProductPremiumRateMapper.toDto(insuranceProductPremiumRate);
        insuranceProductPremiumRateSearchRepository.save(insuranceProductPremiumRate);
        return result;
    }

    /**
     *  Get all the insuranceProductPremiumRates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceProductPremiumRateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InsuranceProductPremiumRates");
        return insuranceProductPremiumRateRepository.findAll(pageable)
            .map(insuranceProductPremiumRateMapper::toDto);
    }

    /**
     *  Get one insuranceProductPremiumRate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InsuranceProductPremiumRateDTO findOne(Long id) {
        log.debug("Request to get InsuranceProductPremiumRate : {}", id);
        InsuranceProductPremiumRate insuranceProductPremiumRate = insuranceProductPremiumRateRepository.findOne(id);
        return insuranceProductPremiumRateMapper.toDto(insuranceProductPremiumRate);
    }

    /**
     *  Delete the  insuranceProductPremiumRate by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsuranceProductPremiumRate : {}", id);
        insuranceProductPremiumRateRepository.delete(id);
        insuranceProductPremiumRateSearchRepository.delete(id);
    }

    /**
     * Search for the insuranceProductPremiumRate corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceProductPremiumRateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InsuranceProductPremiumRates for query {}", query);
        Page<InsuranceProductPremiumRate> result = insuranceProductPremiumRateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(insuranceProductPremiumRateMapper::toDto);
    }

    /**
     * Save Entities
     */
	@Override
	public void saveEntities(List<InsuranceProductPremiumRateDTO> insuranceProductPremiumRateDTOs, 
			Long insuranceProductId) {
		for(InsuranceProductPremiumRateDTO dto : insuranceProductPremiumRateDTOs) {
			switch (dto.getStatus()) {
				case DELETE:
					delete(dto.getId());
					break;
				case UPDATE:
					save(dto);
					break;
				case NEW:
					dto.setInsuranceProductId(insuranceProductId);
					save(dto);
					break;
				default:
					break;
			}
		}
	}
}
