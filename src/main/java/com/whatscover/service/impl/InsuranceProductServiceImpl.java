package com.whatscover.service.impl;

import com.whatscover.service.InsuranceProductService;
import com.whatscover.domain.InsuranceProduct;
import com.whatscover.repository.InsuranceProductRepository;
import com.whatscover.repository.search.InsuranceProductSearchRepository;
import com.whatscover.service.dto.InsuranceProductDTO;
import com.whatscover.service.mapper.InsuranceProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing InsuranceProduct.
 */
@Service
@Transactional
public class InsuranceProductServiceImpl implements InsuranceProductService{

    private final Logger log = LoggerFactory.getLogger(InsuranceProductServiceImpl.class);

    private final InsuranceProductRepository insuranceProductRepository;

    private final InsuranceProductMapper insuranceProductMapper;

    private final InsuranceProductSearchRepository insuranceProductSearchRepository;

    public InsuranceProductServiceImpl(InsuranceProductRepository insuranceProductRepository, InsuranceProductMapper insuranceProductMapper, InsuranceProductSearchRepository insuranceProductSearchRepository) {
        this.insuranceProductRepository = insuranceProductRepository;
        this.insuranceProductMapper = insuranceProductMapper;
        this.insuranceProductSearchRepository = insuranceProductSearchRepository;
    }

    /**
     * Save a insuranceProduct.
     *
     * @param insuranceProductDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InsuranceProductDTO save(InsuranceProductDTO insuranceProductDTO) {
        log.debug("Request to save InsuranceProduct : {}", insuranceProductDTO);
        InsuranceProduct insuranceProduct = insuranceProductMapper.toEntity(insuranceProductDTO);
        insuranceProduct = insuranceProductRepository.save(insuranceProduct);
        InsuranceProductDTO result = insuranceProductMapper.toDto(insuranceProduct);
        insuranceProductSearchRepository.save(insuranceProduct);
        return result;
    }

    /**
     *  Get all the insuranceProducts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InsuranceProducts");
        return insuranceProductRepository.findAll(pageable)
            .map(insuranceProductMapper::toDto);
    }

    /**
     *  Get one insuranceProduct by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InsuranceProductDTO findOne(Long id) {
        log.debug("Request to get InsuranceProduct : {}", id);
        InsuranceProduct insuranceProduct = insuranceProductRepository.findOne(id);
        return insuranceProductMapper.toDto(insuranceProduct);
    }

    /**
     *  Delete the  insuranceProduct by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsuranceProduct : {}", id);
        insuranceProductRepository.delete(id);
        insuranceProductSearchRepository.delete(id);
    }

    /**
     * Search for the insuranceProduct corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InsuranceProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InsuranceProducts for query {}", query);
        Page<InsuranceProduct> result = insuranceProductSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(insuranceProductMapper::toDto);
    }
}
